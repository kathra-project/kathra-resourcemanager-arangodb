# ResourceManager Generator
## How to generate SpringData/ArangoDb stack for KathraResource

### Check config : config.js
 -  coreModelDirectory : Core model source directory path
 -  coreModelPackage : Core model package name
 -  packageRoot : Core model Db package name for class generated
 -  outputDir : Directory for class generated
 -  outputTestDir : Directory for test class generated
 -  templateDir : Directory for templates
 -  strategies : no documented, it's experimental, sorry

#### Example : How to generate resource crud for class Group

```
$ node index.js clazzName=Group

config :  { coreModelDirectory:
   '../../../../../kathra-core/kathra-core-java/kathra-core-model/src/main/java/org/kathra/core/model/',
  coreModelPackage: 'org.kathra.core.model',
  packageRoot: 'org.kathra.resourcemanager',
  outputDir: '../src/main/java/org/kathra/resourcemanager/',
  outputTestDir: '../src/test/java/org/kathra/resourcemanager/',
  templateDir: './templates',
  strategies: { mergeParentProperties: true } }
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupAssignationEdge.java
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupAssignationEdgeRepository.java
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupGroupEdge.java
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupGroupEdgeRepository.java
File generated ../src/main/java/org/kathra/resourcemanager/group//dao/GroupDao.java
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupAssignationEdge.java
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupAssignationEdgeRepository.java
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupGroupEdge.java
File generated ../src/main/java/org/kathra/resourcemanager//group/dao/GroupGroupEdgeRepository.java
File generated ../src/main/java/org/kathra/resourcemanager/group//dao/GroupDb.java
File generated ../src/main/java/org/kathra/resourcemanager/group//dao/GroupRepository.java
File generated ../src/main/java/org/kathra/resourcemanager/group//controller/AbstractGroupsController.java
File generated ../src/main/java/org/kathra/resourcemanager/group//controller/GroupsController.java
File generated ../src/main/java/org/kathra/resourcemanager/group//service/GroupService.java
File generated ../src/test/java/org/kathra/resourcemanager/group//dao/GroupDaoTest.java
File generated ../src/test/java/org/kathra/resourcemanager/group//controller/GroupsControllerTest.java
```

##### For tests
```
mvn -Dtest="org.kathra/resourcemanager/group/**" test
```

##### Overriding
Only GroupService.class and GroupsController.class can be overridden, other files are replaced.