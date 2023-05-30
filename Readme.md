# Local setup

Note: This is a work in progress. Project configurations can look premature and unsecure, but it'll be fine at a later stage.

## MongoDb Setup
Use following docker command to start mongodb on local machine.   

```shell
docker run -d \
-p 27017:27017 \
--name mymongodb \
-e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
-e MONGO_INITDB_ROOT_PASSWORD=admin123 \
mongo
```

Make sure to put proper DB URL in the application.properties
```
mongodb://mongoadmin:admin123@127.0.0.1/admin
```

To have a look at the database. You can use **Studio 3T**

## APIs

### `/upload_csv`
Arguments
- `file` - A CSV file containing groceries data. Checkout sample file `sample_groceries_data.csv`
- `metadata` - A description of file. This is optional