db.createUser({
    user: "test",
    pwd: "test",
    roles: [
        {role: "readWrite", db: "test"}
    ]
});

db.createCollection("resource_objects");