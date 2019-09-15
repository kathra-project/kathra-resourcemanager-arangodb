
// REQUIREMENTS
const fs = require('fs');
const yaml = require('js-yaml');
const path = require('path');
const prettifyJSON = require('prettify-json');
const config = require('./config');
const Swagger2Postman = require("swagger2-postman-generator");
const yamljs = require('yamljs');
const newman = require('newman');
Database = require('arangojs').Database;


var classes = [   "Component",
                "Implementation",
                "ImplementationVersion",
                "ApiVersion",
                "LibraryApiVersion",
                "Library",
                "User",
                "Group",
                "Pipeline",
                "SourceRepository",
                "Assignation"
            ];
var uuids = {   "Component" : "009b8829-cdb9-43d9-a4e4-aa0c00a1f9a8",
                "Implementation" : "35050ac8-7985-479f-837b-7adfd34a4723",
                "ImplementationVersion" : "bc252fbc-ea8b-4e8f-b596-b7d9a5aa929b",
                "ApiVersion" : "af576254-5ded-4008-8554-5e7c0b11536e",
                "LibraryApiVersion" : "1488f080-fff1-4759-b73b-ebf3afaa7bd6",
                "Library" : "bb795c7e-e0d8-43d9-8d08-af1a40c788d6",
                "User" : "c8b329be-0579-437f-b730-ca5f8d007367",
                "Group" : "1f2593ab-aa31-47b5-a409-9086e6237495",
                "Pipeline" : "1e33b17e-c36b-4a02-9c9b-a2772a3ded38",
                "SourceRepository" : "ca0498b5-3c15-40dc-8dec-d325678536ed",
                "Assignation" : "8bf53ef9-f5d5-40e8-9b6e-df098b9bf2fe"
            };


var tests = ['/templates/tests/code200.js'];

var templateCrudResource = fs.readFileSync('./templates/resource.json', 'utf8');

var overridesContent = [];

function generatePostmanProject(config) {
    var postmanProject = null;

    if (config.swaggerFile) {
        postmanProject = JSON   .parse(Swagger2Postman
                                .convertSwagger()
                                .fromJson(JSON.stringify(yamljs.load(config.swaggerFile)))
                                .toPostmanCollectionJson());
    } else if (config.swaggerUri) {
        postmanProject = JSON   .parse(Swagger2Postman
                                .convertSwagger()
                                .fromUrl(config.swaggerUri)
                                .toPostmanCollectionJson());
    } else {
        return 1;
    }


    postmanProject.requests.forEach(function(request) {
        request.url = request.url.replace("{{scheme}}://{{host}}:{{port}}","{{scheme}}://{{host}}:{{port}}/{{context}}");
        request.tests = "pm.test(\"response is ok\", function () { pm.response.to.have.status(200); });";

        overrideRequest(request);


    });

    postmanProject.auth =   {
                                "type": "oauth2",
                                "oauth2": [
                                     {"key": "accessToken","value": "{{token}}","type": "string"},
                                     {"key": "tokenType","value": "bearer","type": "string"},
                                     {"key": "addTokenTo","value": "header","type": "string"}
                                 ]
                            };
    postmanProject.variables = config.postmanProject.environmentVariables;

    fs.writeFileSync(config.outputDir+'/postman.json', JSON.stringify(postmanProject), 'utf8');
}

function overrideRequest(request) {

    var overrideFound = null;
    for(var i in overridesContent) {
        if (request.method.match(overridesContent[i].method) && request.url.match(overridesContent[i].path)) {
            overrideFound = overridesContent[i];
            break;
        }
    }
    if (overrideFound) {
        if (overrideFound.pathVariables) {
            for(var key in overrideFound.pathVariables){
                request.pathVariables[key] = overrideFound.pathVariables[key];
            }
        }
        if (overrideFound.rawModeData) {
            request.rawModeData = JSON.stringify(overrideFound.rawModeData);
        }
        console.log(request.method+" "+request.url);
    } else {

        //templateCustom = replacing(templateCrudResource, "resourcePath", "xx");
    }


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
function getPluralNameForClazz(clazzName) {
    return (clazzName.slice(-1) == "y") ? clazzName.replace(/y$/,"") + "ies" : clazzName + "s";
}


function jsUcfirst(string){
    return string.charAt(0).toUpperCase() + string.slice(1);
}
function jsLcfirst(string){
    return string.charAt(0).toLowerCase() + string.slice(1);
}
var replacing = function(input, key, value) {
    return input.replace(new RegExp('{{'+key+'}}', 'g'), value);
}

function runTest(config) {
    newman.run({
        collection: require(config.outputDir+'/postman.json'),
        reporters: 'cli'
    }, function (err) {
        if (err) { throw err; }
        console.log('collection run complete!');
    });
}


for(var i in classes) {
    var clazz = classes[i];
    var filename = getPluralNameForClazz(clazz).toLowerCase();
    var destPath = './builds/' + filename + '.json';
    if (fs.existsSync('./overrides/' + filename + '.json')) {
        fs.copyFileSync('./overrides/' + filename + '.json', destPath);
    } else {
        var templateCustom = replacing(templateCrudResource, "resourcePath", filename);
        templateCustom = replacing(templateCustom, "resourceIdExisting", uuids[clazz]);
        templateCustom = replacing(templateCustom, "resourceName", "A "+clazz+" example");
        fs.writeFileSync(destPath, templateCustom, 'utf8');
    }

    var overridesContentFile = JSON.parse(fs.readFileSync(destPath, 'utf8'));
    overridesContentFile.forEach(function(item) {
        overridesContent.push(item);
    })
}

generatePostmanProject(config);
//runTest(config);

return 0;
