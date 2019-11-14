const config = {
    coreModelDirectory: '../../kathra-core-model/src/main/java/org/kathra/core/model/',
    coreModelPackage: 'org.kathra.core.model',
    packageRoot : 'org.kathra.resourcemanager',
    outputDir: '../src/main/java/org/kathra/resourcemanager/',
    outputTestDir: '../src/test/java/org/kathra/resourcemanager/',
    templateDir: './templates',
    strategies: {mergeParentProperties: true}
};

module.exports = config;