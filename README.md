{
  "secrets": [
    {
      "data": {
        "postgres": "8jvKWqt1Ln",
        "primaryuser": "gj8FXHTNto",
        "testuser": "LLH0vIv72D"
      },
      "name": "somesecretname"
    }
  ],
  "services": [
    {
      "name": "crunchy-test-cluster-qi-4",
      "spec": {
        "clusterIP": "10.100.200.51",
        "externalIPs": [
          "35.231.232.82"
        ],
        "loadBalancerIP": "35.231.232.82",
        "ports": [
          {
            "name": "postgres",
            "port": 5432,
            "targetPort": 0
          }
        ]
      },
      "status": ""
    }
  ]
}