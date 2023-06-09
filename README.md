
# Rest server to interact with Reddit

A Java REST server for fetching posts from my reddit account and storing them in a MongoDB database, performing CRUD operationd on MongoDB database using Spring data and posting posts through the reddit API.

## Endpoints
* **GET /auth** -> for authentication of reddit API
* **GET /loadData** -> to fetch my upvoted posts from reddit and store them into MongoDB database.
* **GET /queryDoc/{query}** -> to get the posts containing the give query.
* **DELETE /deleteDoc/{username}** -> to delete the posts from MongoDB posted by the given user.
* **GET /findDocbyUser/{username}** -> to get the posts from MongoDB posted by the giver user.
* **GET /sortbyTime** -> to sort the posts by time of posting (recent first)
* **POST /createPost** -> to post a message on reddit. The body of the request should contain subreddit, title and text. The post would get stored into the MongoDB database automatically.
 
