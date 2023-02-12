# Description
An application intended to store images/documents of receipts and associated metadata. Allows for easier receipt 
retrieval when claiming insurance or to keep track of various purchased items.

# Running
This is a Spring Boot application. Will soon be Dockerised. Future steps will be written then.

# Deployment
The Spring Boot application is a typical stateless REST-API service. It relies on a SQL database (currently in-memory as
of the time of writing). As of the time of writing, it currently stores receipt files on server (so not completely 
stateless). In future, these files should be stored on an S3 bucket or a shared network hard-drive (like AWS EFS).