
const newman = require('newman');
const request = require('request');
const querystring = require('querystring');
const http = require('http');
const fs = require('fs');

function getAccessToken(callback) {

    // form data
    var postData = querystring.stringify({
            grant_type: 'password',
            client_id: process.env.OAUTH2_CLIENT_ID,
            client_secret: process.env.OAUTH2_CLIENT_SECRET,
            username: process.env.OAUTH2_LOGIN,
            password: process.env.OAUTH2_PASSWORD
    });

    // request option
    var options = {
        host: process.env.OAUTH2_HOST,
        port: process.env.OAUTH2_PORT,
        path: process.env.OAUTH2_PATH,
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Content-Length': postData.length
        }
    };

    // request object
    var req = http.request(options, function (res) {
      var result = '';
      res.on('data', function (chunk) {
        result += chunk;
      });
      res.on('end', function () {
        callback(JSON.parse(result));
      });
      res.on('error', function (err) {
        console.error(err);
      })
    });

    req.write(postData);
    req.end();

}

function execTest(token){
    console.log(token);


    var environmentVariables = [{
                                    "id": "547cfc59-ea7a-4fe0-84bb-3499dc03af18",
                                    "key": "scheme",
                                    "value": "http",
                                    "type": "string"
                                },
                                {
                                    "id": "75ca65b0-61bf-4f71-8034-89dc083930d3",
                                    "key": "host",
                                    "value": process.env.RESOURCEMANAGER_HOST,
                                    "type": "string"
                                },
                                {
                                    "id": "d3da4a69-40eb-4932-ae90-0b3c04b82ab0",
                                    "key": "port",
                                    "value": process.env.RESOURCEMANAGER_PORT,
                                    "type": "string"
                                },
                                {
                                    "id": "3cbda410-6a8e-4a95-9452-96af77a43877",
                                    "key": "context",
                                    "value": process.env.RESOURCEMANAGER_CONTEXT_PATH,
                                    "type": "string"
                                },
                                {
                                    "id": "3cbda410-6a8e-4a95-9452-96af77a43878",
                                    "key": "token",
                                    "value": token.access_token,
                                    "type": "string"
                                }
                            ];

    var postmanProject = JSON.parse(fs.readFileSync('./postman.json', 'utf8'));
    for(var i in postmanProject.variables) {
        variable = postmanProject.variables;
        switch(postmanProject.variables[i].key) {
            case "token":
                postmanProject.variables[i].value = token.access_token;
                break;
            case "context":
                postmanProject.variables[i].value = process.env.RESOURCEMANAGER_CONTEXT_PATH;
                break;
            case "port":
                postmanProject.variables[i].value = process.env.RESOURCEMANAGER_PORT;
                break;
            case "host":
                postmanProject.variables[i].value = process.env.RESOURCEMANAGER_HOST;
                break;
        }
    }


    fs.writeFileSync('./postman-with-env-var.json', JSON.stringify(postmanProject), 'utf8');
    /*
    newman.run({
        collection: require('./postman-with-env-var.json'),
        reporters: 'cli'
    }, function (err) {
        if (err) { throw err; }
        console.log('collection run complete!');
    });*/
}

getAccessToken(execTest);


return 0;