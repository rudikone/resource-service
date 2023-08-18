db.createUser({
    user: "test",
    pwd: "test",
    roles: [
        {role: "readWrite", db: "test"}
    ]
});

db.createCollection("resource_objects");
db.createCollection("users");

db.users.insertMany([
    {
        "_id": "1",
        "name": "Rudikov",
        "age": 28,
        "salary": 1000000,
        "department": "IT",
        "login": "admin",
        "password": "admin",
        "roles": ["ADMIN"]
    },
    {
        "_id": "2",
        "name": "Ivanov",
        "age": 28,
        "salary": 1000000,
        "department": "IT",
        "login": "user",
        "password": "user",
        "roles": ["USER"]
    }
]);