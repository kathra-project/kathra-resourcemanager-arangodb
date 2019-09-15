const config = {
    swaggerUri: 'http://localhost:8080/api/v1/swagger.json',
    swaggerFile: '../../../kathra-resourcemanager-api/swagger.yml',
    outputDir: '../src/test/e2e/e2e-docker/src',
    postmanProject: {
        environmentVariables : [
                               		{
                               			"id": "547cfc59-ea7a-4fe0-84bb-3499dc03af18",
                               			"key": "scheme",
                               			"value": "http",
                               			"type": "string"
                               		},
                               		{
                               			"id": "75ca65b0-61bf-4f71-8034-89dc083930d3",
                               			"key": "host",
                               			"value": "localhost",
                               			"type": "string"
                               		},
                               		{
                               			"id": "d3da4a69-40eb-4932-ae90-0b3c04b82ab0",
                               			"key": "port",
                               			"value": "8080",
                               			"type": "string"
                               		},
                               		{
                               			"id": "3cbda410-6a8e-4a95-9452-96af77a43877",
                               			"key": "context",
                               			"value": "api/v1",
                               			"type": "string"
                               		},
                                    {
                                        "id": "3cbda410-6a8e-4a95-9452-96af77a43878",
                                        "key": "token",
                                        "value": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJSblhwLUpDLVRHajJtUkdjdm92dmhZb1pyck5qOFBHdnpseEg1dkcwQkJRIn0.eyJqdGkiOiI2MWM0OGNhZS1jZGQyLTQ2NjMtYTE3OS1jMjMyZTEwYTUxYTQiLCJleHAiOjE1NDcxOTc4OTgsIm5iZiI6MCwiaWF0IjoxNTQ3MTExNDk4LCJpc3MiOiJodHRwczovL2tleWNsb2FrLmRldi1pcnRzeXN4LmZyL2F1dGgvcmVhbG1zL3NtaXRlIiwiYXVkIjoic21pdGUtcmVzb3VyY2VtYW5hZ2VyIiwic3ViIjoiZGJhYWZjNmEtOGQ3Yi00Nzc5LWEzYTUtN2UzM2RjNWVhNTc5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic21pdGUtcmVzb3VyY2VtYW5hZ2VyIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiMWRhYjI0YjUtZDY3MC00MzIyLThmZjYtYTg3OWVlY2FmNGRkIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiSnVsaWVuIEJPVUJFQ0hUT1VMQSIsImdyb3VwcyI6WyIvSU5UIiwiL0xTVF9DT01NVU4iLCIvV2lmaV9Nb3Vsb24iLCIvRGlyZWN0aW9uIFRlY2huaXF1ZSIsIi9SSCIsIi9XaWZpX05hbm8iLCIvVmxhbi00MTAgUHJvamV0cyIsIi9DRSIsIi9SV19DT01NVU5fRFQiLCIvT3BlbnN0YWNrVXNlcnMiLCIvamVua2lucy1hZG1pbiIsIi9DSFNDVCJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqdWxpZW4uYm91YmVjaHRvdWxhIiwiZ2l2ZW5fbmFtZSI6Ikp1bGllbiIsImZhbWlseV9uYW1lIjoiQk9VQkVDSFRPVUxBIiwiZW1haWwiOiJqdWxpZW4uYm91YmVjaHRvdWxhQGlydC1zeXN0ZW14LmZyIn0.OAv1xfyrtvyRnTf0uKTl31BGsRdA2VEuCkgbJW2C8ew5H2Sj3k4kTUNI3XzNZ8y5rIzYs-zP3XKMOUqQzq9kST1GuRpVPPbNtpd1j0-vyj6Zn_wv1PxiwIm3XPU1Z6JLBq3Pt2SfRD3DvMeLvtUiGfetCXtmRXeRy-F6hlsW2E57GGeKBScrpDUJeB7C0TlVRSOGzCfGKVPHZkAi3vh3eGHLLUBm4ISi86S2YOZXcXPlAhjEyxJPKNU5g8jRRFwuTSZ95SkvYfZVzEVXTGF96dixMlGAdEewCMTSd1qjWdsLK-pb9Hxd2slF19o2j_sZAeEvKbpTlWNPh5ZYvqEsOQ",
                                        "type": "string"
                                    }
                               	]
    }
};

module.exports = config;