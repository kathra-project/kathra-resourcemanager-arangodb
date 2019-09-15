
// REQUIREMENTS
const fs = require('fs');
const yaml = require('js-yaml');
const path = require('path');
const javaParser = require("java-parser");

const config = require('./config');
console.debug('config : ', config);


var clazzNames = null;

var author = require("os").userInfo().username;
var programPackageJSON = JSON.parse(fs.readFileSync('package.json', 'utf8'));

var files = [
            '/dao/Abstract{{clazz}}Dao.java',
            '/dao/{{clazz}}Dao.java',
            '/dao/{{clazz}}Db.java',
            '/dao/{{clazz}}Mapper.java',
            '/dao/{{clazz}}Repository.java',
            '/controller/Abstract{{clazzPlural}}Controller.java',
            '/controller/{{clazzPlural}}Controller.java',
            '/service/{{clazz}}Service.java'];

var filesTest = ['/dao/{{clazz}}DaoTest.java',
            '/controller/{{clazzPlural}}ControllerTest.java',
            '/service/{{clazz}}ServiceTest.java'];

var edgeTemplate = [''];

var templateUpdateEdge = "/main/dao/{{clazzA}}{{clazzB}}Edge.java";
var templateUpdateEdgeRepository = "/main/dao/{{clazzA}}{{clazzB}}EdgeRepository.java";
var templateUpdateEdgeList = '/main/dao/EdgeUpdateList';
var templateUpdateEdgeReference = '/main/dao/EdgeUpdateReference';


var templateUpdateEdgeListData = fs.readFileSync(config.templateDir+templateUpdateEdgeList, 'utf8');
var templateUpdateEdgeReferenceData = fs.readFileSync(config.templateDir+templateUpdateEdgeReference, 'utf8');

process.argv.forEach(function (val, index, array) {
    if (val.split("=")[0] == 'clazzName') {
        clazzNames = val.split("=")[1].split(',');
    }
});

if (clazzNames == null) {
    console.error("clazzName is not defined");
    return 1;
}



function generateArangoDbStack(clazzName, config, modelClass, options) {
    var packageName = clazzName.toLowerCase();
    var packageFullName = config.packageRoot + '.' + packageName;
    var packageDirectory = config.outputDir + packageName;
    var packageTestDirectory = config.outputTestDir + packageName;

    var javaModel = javaParser.parse(fs.readFileSync(modelClass, 'utf8'));
    var parentClazzName = getParentClassFromModel(javaModel);
    var parentDbClazzName = 'AbstractResourceDb';


    if (!config.strategies.mergeParentProperties && parentClazzName != 'Resource') {
        // generate parent class previously
        generateArangoDbStack(parentClazzName, config.outputDir, config.coreModelDirectory+'/'+parentClazzName+'.java', {modelOnly:true, isAbstract:true});
        parentDbClazzName = parentClazzName + 'Db';

    }

    var propertiesModel = getPropertiesModel(javaModel, config.strategies.mergeParentProperties);
    definePropertiesModelConvertedToDbResource(clazzName, propertiesModel);

    var directories = ["dao", "controller", "service"];
    directories.forEach(function(directory) {
        if (!fs.existsSync(packageDirectory + "/" + directory)) {
            fs.mkdirSync(packageDirectory + "/" + directory, {recursive :true});
        }
        if (!fs.existsSync(packageTestDirectory + "/" + directory)) {
            fs.mkdirSync(packageTestDirectory + "/" + directory, {recursive :true});
        }
    });

    files.forEach(function(file) {
        if (options && options.modelOnly && file != '/dao/{{clazz}}Db.java') {
            return;
        }

        var srcPath = config.templateDir+"/main/"+file;
        var destPath = packageDirectory+"/"+replacingClazz(file, clazzName);

        var templateData = fs.readFileSync(srcPath, 'utf8');
        var generateNewFile = true;
        switch(file) {
            case '/dao/{{clazz}}Db.java':
                templateData = getTemplateDbModel(config, options, clazzName, packageFullName, parentClazzName, parentDbClazzName, templateData, propertiesModel);
                propertiesModel.filter(property => property.edge).forEach(function(property) {
                    createEdgeClass(property.edge, config.outputDir);
                });
            break;
            case '/dao/{{clazz}}Dao.java':
                if (fs.existsSync(packageDirectory + replacingClazz(file, clazzName))) {
                    generateNewFile = false;
                    break;
                }
                templateData = getTemplateDao(config, javaModel, clazzName, templateData);
            break;
            case '/dao/Abstract{{clazz}}Dao.java':
                templateData = getTemplateDao(config, javaModel, clazzName, templateData);
            break;
            case '/controller/{{clazzPlural}}Controller.java':
                if (fs.existsSync(packageDirectory + replacingClazz(file, clazzName))) {
                    generateNewFile = false;
                    break;
                }
            break;
            case '/service/{{clazz}}Service.java':
                if (fs.existsSync(packageDirectory + replacingClazz(file, clazzName))) {
                    generateNewFile = false;
                    break;
                }
            break;
        }
        if (!generateNewFile)
            return;
        templateData = genericTemplatingOperations(templateData, packageFullName, config, author, clazzName);
        fs.writeFileSync(destPath, templateData, 'utf8');
        console.debug("File generated " + destPath);
    });

    filesTest.forEach(function(file) {
        if (options && options.modelOnly) {
            return;
        }

        var srcPath = config.templateDir + "/test/" + file;
        var destPath = packageTestDirectory + "/" + replacingClazz(file, clazzName);
        var templateData = fs.readFileSync(srcPath, 'utf8');
        var generateNewFile = true;
        switch(file) {
            case '/dao/{{clazz}}DaoTest.java':
                var toImport = "";
                var fields = "";
                var beforeEach = "";
                propertiesModel.filter(property => property.edge).forEach(function(property) {
                    //toImport += getImport(config.packageRoot + '.' + typeProperty.toLowerCase() + '.dao.' + typeDbProperty)+"\n";
                    toImport += getImport(property.edge.package + '.' + property.edge.clazzName)+"\n";
                    toImport += getImport(property.edge.package + '.' + property.edge.clazzNameRepository)+"\n";

                    fields += '\t@Mock\n';
                    fields += '\t' + property.edge.clazzNameRepository + ' ' + jsLcfirst(property.edge.clazzNameRepository) + ';\n';

                    beforeEach += '\t\tunderTest.' + jsLcfirst(property.edge.clazzNameRepository) + ' = ' + jsLcfirst(property.edge.clazzNameRepository) + ';\n';
                });

                templateData = replacing(templateData, 'imports', toImport);
                templateData = replacing(templateData, 'fields', fields);
                templateData = replacing(templateData, 'beforeEach', beforeEach);
            break;
            case '/service/{{clazz}}ServiceTest.java':
                if (fs.existsSync(packageTestDirectory + replacingClazz(file, clazzName))) {
                    generateNewFile = false;
                    break;
                }
            break;
        }
        if (!generateNewFile)
            return;
        templateData = genericTemplatingOperations(templateData, packageFullName, config, author, clazzName);
        fs.writeFileSync(destPath, templateData, 'utf8');
        console.debug("File generated " + destPath);
    });

    return true;
}

function genericTemplatingOperations(templateData, packageFullName, config, author, clazzName) {
    templateData = replacing(templateData, 'package', packageFullName);
    templateData = replacing(templateData, 'coreModelPackage', config.coreModelPackage);
    templateData = replacing(templateData, 'author', author);
    templateData = replacing(templateData, 'generatorSignature', getSignature());
    templateData = replacingClazz(templateData, clazzName);
    return templateData;
}

function getTemplateDbModel(config, options, clazzName, packageFullName, parentClazzName, parentDbClazzName, templateData, propertiesModel) {
    var toImport = generateImports(clazzName, propertiesModel);
    toImport += getImport(config.coreModelPackage + '.' + parentClazzName + '.*')+"\n"

    var clazzTypesParameters = (options && options.isAbstract) ? '<X extends Resource>' : '';
    var parentClazzTypesParameters = (options && options.isAbstract) ? '<X>' : '<' + clazzName + '>';

    templateData = replacing(templateData, 'clazzTypesParameters', clazzTypesParameters);
    templateData = replacing(templateData, 'parentClazzTypesParameters', parentClazzTypesParameters);

    if (options && options.isAbstract) {
        toImport += getImport(config.coreModelPackage + '.Resource')+"\n";
    }

    templateData = replacing(templateData, 'fields', generateFields(clazzName, propertiesModel));
    templateData = replacing(templateData, 'parentClazz', parentDbClazzName);
    templateData = replacing(templateData, 'methods', generateGetterSetter(propertiesModel));
    templateData = replacing(templateData, 'methods', generateGetterSetter(propertiesModel));
    templateData = replacing(templateData, 'clazzAnnotations', (options && options.isAbstract) ? "" : replacingClazz("@Document(\"{{clazzPlural}}\")", clazzName));

    propertiesModel.forEach(function(property) {
        if (property.edge && property.edge.package != packageFullName) {
            toImport += getImport(property.edge.package + '.' + property.edge.clazzName)+"\n";
        }
    });
    if (parentDbClazzName != 'AbstractResourceDb') {
        toImport += getImport(config.packageRoot + '.' + parentClazzName.toLowerCase() + '.dao.' + parentDbClazzName)+"\n";
    }

    return replacing(templateData, 'imports', toImport);
}

function getTemplateDao(config, javaModel, clazzName, templateData) {
    var fields = "";
    var createOverride = "";
    var initCollectionIfNotExistOverride = "";
    var updateOverride = "";
    var toImport = "";
    var properties = getPropertiesModel(javaModel, true);
    definePropertiesModelConvertedToDbResource(clazzName, properties);
    properties.filter(property => property.edge).forEach(function(property) {
        var templateEdgeUpdate = property.isList ? templateUpdateEdgeListData : templateUpdateEdgeReferenceData;
        var typeProperty = property.isList ? property.parameterizedType.type : property.type;
        var typeDbProperty = property.isList ? property.parameterizedType.typeDb : property.typeDb;

        fields += "\t@Autowired\n\t" + property.edge.clazzNameRepository + " " + jsLcfirst(property.edge.clazzNameRepository)+";\n";
        initCollectionIfNotExistOverride += "\t\tthis."+jsLcfirst(property.edge.clazzNameRepository)+".count();\n";
        createEdgeClass(property.edge, config.outputDir);

        templateEdgeUpdate = replacingClazz(templateEdgeUpdate, clazzName);
        templateEdgeUpdate = replacing(templateEdgeUpdate, 'propertyGetter', "get"+jsUcfirst(property.name));
        templateEdgeUpdate = replacing(templateEdgeUpdate, 'property', property.name);
        templateEdgeUpdate = replacing(templateEdgeUpdate, 'clazzProperty', typeDbProperty);
        templateEdgeUpdate = replacing(templateEdgeUpdate, 'clazzDb', clazzName+"Db");
        templateEdgeUpdate = replacing(templateEdgeUpdate, 'edgeRepository', jsLcfirst(property.edge.clazzNameRepository));
        templateEdgeUpdate = replacing(templateEdgeUpdate, 'edgeClazz', property.edge.clazzName);

        updateOverride += templateEdgeUpdate+'\n';
        if (!property.isList) {
            createOverride += templateEdgeUpdate+'\n';
        }
        toImport += getImport(config.packageRoot + '.' + typeProperty.toLowerCase() + '.dao.' + typeDbProperty)+"\n";
        toImport += getImport(property.edge.package + '.' + property.edge.clazzName)+"\n";
        toImport += getImport(property.edge.package + '.' + property.edge.clazzNameRepository)+"\n";
    });
    templateData = replacing(templateData, 'fields', fields);
    templateData = replacing(templateData, 'createOverride', createOverride);
    if (initCollectionIfNotExistOverride != "") {
        initCollectionIfNotExistOverride = "//init edge repositories \n" + initCollectionIfNotExistOverride;
    }
    templateData = replacing(templateData, 'initCollectionIfNotExistOverride', initCollectionIfNotExistOverride);
    templateData = replacing(templateData, 'updateOverride', updateOverride);
    templateData = replacing(templateData, 'imports', toImport);
    return templateData;
}

function getSignature(){
    return programPackageJSON.name + "@" + programPackageJSON.version+" at " + new Date().toISOString();
}

function generateImports(clazzName, properties) {
    var notImports = ["String", "Integer", "List", "Map", "Boolean"];
    var classesToImport = [];
    properties.forEach(function (property){
        var classToImport = generateImportsProperty(clazzName, property, classesToImport);
        if (classToImport != null) {
            classesToImport.push(classToImport);
        }
    });

    var toImports = "";
    classesToImport.forEach(function (property){
        toImports += getImport(property.packageName+"."+property.clazzName)+"\n";
    });

    return toImports;
}

function getImport(clazzFullName) {
    return "import "+clazzFullName+";";
}

function generateImportsProperty(clazzName, property, alreadyImport) {
    var notImports = ["String", "Integer", "List", "Map", "Boolean"];
    if (property.type == clazzName) {
        return null;
    }
    if (notImports.indexOf(property.type) >= 0) {
        return null;
    }
    if (alreadyImport.map(i => i.clazzName).indexOf(property.type) >= 0){
        return null;
    }

    if (property.isList) {
        return generateImportsProperty(clazzName, property.parameterizedType, alreadyImport);
    } else if (property.typeDb != null) {
        return {clazzName:property.typeDb, packageName: config.packageRoot+'.' + property.type.toLowerCase() + '.dao'};
    } else {
        return fs.existsSync(config.coreModelDirectory+'/'+property.type+'.java') ? {clazzName:property.type, packageName: config.coreModelPackage} : null;
    }
}

function checkClassIsResource(clazzName) {
    try {
        var javaModel = javaParser.parse(fs.readFileSync(config.coreModelDirectory+'/'+clazzName+'.java', 'utf8'));
        var superClass = getParentClassFromModel(javaModel);
        if (superClass == 'Resource') {
            return true;
        } else {
            return checkClassIsResource(superClass);
        }
    } catch(err) {

    }
    return false;
}

function generateGetterSetter(properties) {

    var getterStetterMethods = "";
    properties.forEach(function (property){
        var clazzName = property.typeDb != null ? property.typeDb : property.type;
        var propertyName = property.name;

        getterStetterMethods += "\t\tpublic "+clazzName+" get"+jsUcfirst(propertyName)+"() { return this."+propertyName+";}\n";
        getterStetterMethods += "\t\tpublic void set"+jsUcfirst(propertyName)+"("+clazzName+" "+propertyName+") { this."+propertyName+"="+propertyName+";}\n\n";
    });
    return getterStetterMethods;
}

function jsUcfirst(string){
    return string.charAt(0).toUpperCase() + string.slice(1);
}
function jsLcfirst(string){
    return string.charAt(0).toLowerCase() + string.slice(1);
}

function generateFields(clazzName, properties) {
    var fields = "";
    properties.forEach(function (property){
        var clazzName = property.typeDb != null ? property.typeDb : property.type;
        if (property.isList) {
            if ( property.parameterizedType.typeDb) {
                fields += "\t\t@Relations(edges = "+property.edge.clazzName+".class, lazy = true) \n";
                fields += "\t\tprivate List<"+property.parameterizedType.typeDb+"> "+property.name+"; \n";
            } else {
                fields += "\t\tprivate List<"+property.parameterizedType.type+"> "+property.name+"; \n";
            }
        } else if (property.typeDb) {
            fields += "\t\t@Relations(edges = "+property.edge.clazzName+".class, lazy = true) \n";
            fields += "\t\tprivate "+property.typeDb+" "+property.name+"; \n";
        } else {
            fields += "\t\tprivate "+property.type+" "+property.name+"; \n";
        }
    });
    return fields;
}

function createEdgeClass(edge, outputDir) {

    var srcPath = config.templateDir+"/"+templateUpdateEdge;
    var destDir = outputDir + '/' + edge.firstClazz.toLowerCase() + '/dao/';
    var destPath = destDir + edge.clazzName+".java";
    if (!fs.existsSync(destDir)) {
        fs.mkdirSync(destDir, {recursive :true});
    }

    var templateData = fs.readFileSync(srcPath, 'utf8');
        templateData = replacing(templateData, 'clazz', edge.clazzName);
        templateData = replacing(templateData, 'clazzA', edge.firstClazz);
        templateData = replacing(templateData, 'clazzB', edge.secondClazz);
        templateData = replacing(templateData, 'propertyA', edge.firstProperty);
        templateData = replacing(templateData, 'propertyB', edge.secondProperty);
        templateData = replacing(templateData, 'PropertyA', jsUcfirst(edge.firstProperty));
        templateData = replacing(templateData, 'PropertyB', jsUcfirst(edge.secondProperty));
        templateData = replacing(templateData, 'package', edge.package);
        templateData = replacing(templateData, 'author', author);
        templateData = replacing(templateData, 'generatorSignature', getSignature());

    var imports = generateImports(edge.clazzName, [{type: edge.firstClazz, typeDb:edge.firstClazz+"Db"},{type: edge.secondClazz, typeDb:edge.secondClazz+"Db"}]);
        templateData = replacing(templateData, 'imports', imports);

    fs.writeFileSync(destPath, templateData, 'utf8');
    console.debug("File generated " + destPath);
    createEdgeRepositoryClass(edge, outputDir);
}

function createEdgeRepositoryClass(edge, outputDir) {

    var file = "{{clazzA}}{{clazzB}}EdgeRepository.java";
    var srcPath = config.templateDir+"/"+templateUpdateEdgeRepository;
    var destDir = outputDir+"/"+edge.firstClazz.toLowerCase()+"/dao/";
    var destPath = destDir+edge.clazzNameRepository+".java";
    if (!fs.existsSync(destDir)) {
        fs.mkdirSync(destDir, {recursive :true});
    }

    var templateData = fs.readFileSync(srcPath, 'utf8');
        templateData = replacing(templateData, 'clazz', edge.clazzName);
        templateData = replacing(templateData, 'clazzA', edge.firstClazz);
        templateData = replacing(templateData, 'clazzB', edge.secondClazz);
        templateData = replacing(templateData, 'propertyA', jsUcfirst(edge.firstProperty));
        templateData = replacing(templateData, 'propertyB', jsUcfirst(edge.secondProperty));
        templateData = replacing(templateData, 'package', edge.package);
        templateData = replacing(templateData, 'author', author);
        templateData = replacing(templateData, 'generatorSignature', getSignature());

    fs.writeFileSync(destPath, templateData, 'utf8');
    console.debug("File generated " + destPath);
}

function getPropertiesModel(javaModel, mergeParentProperties) {
    var properties = [];
    for(i in javaModel.types[0].bodyDeclarations) {
        var item = javaModel.types[0].bodyDeclarations[i];
        if (item.node == 'FieldDeclaration') {
            var property = {};
            property.name = item.fragments[0].name.identifier;
            property.classReference = javaModel.types[0].name.identifier;
            var propertyType = null;
            if (item.type.node == 'SimpleType') {
                property.type = item.type.name.identifier;
            } else if (item.type.node == 'ParameterizedType') {
                property.type = item.type.type.name.identifier+"<"+item.type.typeArguments[0].name.identifier+">";
                property.isList = (item.type.type.name.identifier == 'List');
                property.parameterizedType = {type: item.type.typeArguments[0].name.identifier};
            }
            properties.push(property);
        }
    }

    var parentClazzName = getParentClassFromModel(javaModel);
    if (mergeParentProperties == true && parentClazzName != 'Resource') {
        var parentModel = javaParser.parse(fs.readFileSync(config.coreModelDirectory+'/'+parentClazzName+'.java', 'utf8'));
        parentsProperties = getPropertiesModel(parentModel, mergeParentProperties);
        properties = properties.concat(parentsProperties);
    }
    return properties;
}

function getParentClassFromModel(javaModel) {
    return javaModel.types[0].superclassType.name.identifier;
}

function definePropertiesModelConvertedToDbResource(clazzName, properties) {
    properties.forEach(function (property){
        if (property.parameterizedType != null) {
            if (checkClassIsResource(property.parameterizedType.type)) {
                property.parameterizedType.typeDb = property.parameterizedType.type + 'Db';
            }
            if (property.isList && property.parameterizedType.typeDb) {
                property.typeDb = 'List<' + property.parameterizedType.typeDb + '>';
                property.edge = getEdge(clazzName, property.parameterizedType.type);
            } else if (property.isList && property.parameterizedType.type) {
                property.typeDb = 'List<' + property.parameterizedType.type + '>';
            }
        } else if (checkClassIsResource(property.type)) {
            property.typeDb = property.type + 'Db';
            property.edge = getEdge(clazzName, property.type);
        }
    });
}

function getEdge(clazzA, clazzB) {
    var edge = {};

    edge.firstClazz  = clazzA.localeCompare(clazzB) > 0 ? clazzA : clazzB;
    edge.secondClazz = clazzA.localeCompare(clazzB) > 0 ? clazzB : clazzA;

    edge.clazzName = edge.firstClazz+edge.secondClazz+"Edge";
    edge.clazzNameRepository = edge.firstClazz+edge.secondClazz+"EdgeRepository";
    edge.package = config.packageRoot + '.' + edge.firstClazz.toLowerCase() + '.dao';

    if (edge.firstClazz == edge.secondClazz) {
        edge.firstProperty = 'parent';
        edge.secondProperty = 'child';
    } else {
        edge.firstProperty = jsLcfirst(edge.firstClazz);
        edge.secondProperty = jsLcfirst(edge.secondClazz);
    }
    return edge;
}

function copyFolderRecursiveSync( source, target ) {
    var files = [];

    //check if folder needs to be created or integrated
    var targetFolder = target;
    if ( !fs.existsSync( targetFolder ) ) {
        fs.mkdirSync( targetFolder );
    }

    //copy
    if ( fs.lstatSync( source ).isDirectory() ) {
        files = fs.readdirSync( source );
        files.forEach( function ( file ) {
            var curSource = path.join( source, file );
            if ( fs.lstatSync( curSource ).isDirectory() ) {
                copyFolderRecursiveSync( curSource, targetFolder );
            } else {
                copyFileSync( curSource, targetFolder );
            }
        } );
    }
}


function copyFileSync( source, target ) {
    console.debug("Copy file " + source + " into directory " + target);
    var targetFile = target;
    //if target is a directory a new file with the same name will be created
    if ( fs.existsSync( target ) ) {
        if ( fs.lstatSync( target ).isDirectory() ) {
            targetFile = path.join( target, path.basename( source ) );
        }
    }
    fs.writeFileSync(targetFile, fs.readFileSync(source));
}


var replacing = function(input, key, value) {
    return input.replace(new RegExp('{{'+key+'}}', 'g'), value);
}

/**
Replace class's references

@input : text
@clazzName : class's name
@return : text modified
*/
var replacingClazz = function(input, clazzName) {
    var out = replacing(input, 'clazz', clazzName);
    var clazzNamePural = getPluralNameForClazz(clazzName);
    var out = replacing(out, 'clazzPlural', clazzNamePural);
    var out = replacing(out, 'clazzLower', clazzName.toLowerCase());
    var out = replacing(out, 'clazzPluralLower', clazzNamePural.toLowerCase());
    return out;
}
/**

@clazzName : class's name
@return : plural's class name
*/
var getPluralNameForClazz = function(clazzName) {
    return (clazzName.slice(-1) == "y") ? clazzName.replace(/y$/,"") + "ies" : clazzName + "s";
}


clazzNames.forEach(function(clazzName){
    generateArangoDbStack(clazzName, config, config.coreModelDirectory+'/'+clazzName+'.java');
});

return 0;
