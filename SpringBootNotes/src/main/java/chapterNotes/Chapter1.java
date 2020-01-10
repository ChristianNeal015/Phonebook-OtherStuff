package chapterNotes;

public class Chapter1 {
    /*
    Representation:

    These
    representations can range from text-based HTML, XML, and JSON formats to binary formats such as PDFs,
    JPEGs, and MP4s. It is possible for the client to request a particular representation and this process is termed
    as content negotiation. Here are the two possible content negotiation strategies:

•	 Postfixing the URI with the desired representation—In this strategy, a client
    requesting product details in JSON format would use the URI http://www.example.
    com/products/143.json. A different client might use the URI http://www.example.
    com/products/143.xml to get product details in XML format.

•	 Using the Accept header—Clients can populate the HTTP Accept header with
    the desired representation and send it along with the request. The application
    handling the resource would use the Accept header value to serialize the requested
    representation. The RFC 26163
    provides a detailed set of rules for specifying one or
    more formats and their priorities

    HTTP Methods:

    The “Uniform Interface” constraint restricts the interactions between client and server through a handful of
    standardized operations or verbs. On the Web, the HTTP standard4
    provides eight HTTP methods that allow
    clients to interact and manipulate resources. Some of the commonly used methods are GET, POST, PUT, and
    DELETE. Before we delve deep in to HTTP methods, let’s review their two important characteristics—safety
    and idempotency.
3
    http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1. 4
    https://www.ietf.org/rfc/rfc2616.txt.

    Safety:

    A HTTP method is said to be safe if it doesn’t cause any changes to the server state. Consider methods such
    as GET or HEAD, which are used to retrieve information/resources from the server. These requests are
    typically implemented as read-only operations without causing any changes to the server’s state and, hence,
    considered safe.

    Safe methods are used to retrieve resources. However, safety doesn’t mean that the method must return
    the same value every time. For example, a GET request to retrieve Google stock might result in a different
    value for each call. But as long as it didn’t alter any state, it is still considered safe.

    In real-world implementations, there may still be side effects with a safe operation. Consider the
    implementation in which each request for stock prices gets logged in a database. From a purist perspective
    we are changing the state of the entire system. However, from a practical standpoint, because these side
    effects were the sole responsibility of the server implementation, the operation is still considered to be safe.

    Idempotency:

    An operation is considered to be idempotent if it produces the same server state whether we apply it once
    or any number of times. HTTP methods such as GET, HEAD (which are also safe), PUT, and DELETE are
    considered to be idempotent, guaranteeing that clients can repeat a request and expect the same effect as
    making the request once. The second and subsequent requests leave the resource state in exactly the same
    state as the first request did.

    Consider the scenario in which you are deleting an order in an ecommerce application. On successful
    completion of the request, the order no longer exists on the server. Hence, any future requests to delete that
    order would still result in the same server state. By contrast, consider the scenario in which you are creating
    an order using a POST request. On successful completion of the request, a new order gets created. If you
    were to re“POST” the same request, the server simply honors the request and creates a new order. Because a
    repeated POST request can result in unforeseen side effects, POST is not considered to be idempotent.

    Get:

    The GET method is used to retrieve a resource’s representation. For example, a GET on the URI http://
    blog.example.com/posts/1 returns the representation of the blog post identified by 1. By contrast, a GET on
    the URI http://blog.example.com/posts retrieves a collection of blog posts. Because GET requests don’t
    modify server state, they are considered to be safe and idempotent.
    A hypothetical GET request to http://blog.example.com/posts/1 and the corresponding response are
    shown here.

    Head:

    On occasions, a client would like to check if a particular resource exists and doesn’t really care about the
    actual representation. In another scenario, the client would like to know if a newer version of the resource is
    available before it downloads it. In both cases, a GET request could be “heavyweight” in terms of bandwidth
    and resources. Instead, a HEAD method is more appropriate.

    The HEAD method allows a client to only retrieve the metadata associated with a resource. No resource
    representation gets sent to the client. This metadata represented as HTTP headers will be identical to
    the information sent in response to a GET request. The client uses this metadata to determine resource
    accessibility and recent modifications. Here is a hypothetical HEAD request and the response.


    Delete:

    The DELETE method, as the name suggests, requests a resource to be deleted. On receiving the request, a
    server deletes the resource. For resources that might take a long time to delete, the server typically sends a
    confirmation that it has received the request and will work on it. Depending on the service implementation,
    the resource may or may not be physically deleted.

    On successful deletion, future GET requests on that resource would yield a “Resource Not Found” error
    via HTTP status code 404. We will be covering status codes in just a minute.
    In this example, the client requests a post identified by 1 to be deleted. On completion, the server could
    return a status code 200 (OK) or 204 (No Content), indicating that the request was successfully processed

    Delete /posts/1 HTTP/1.1
    Content-Length: 0
    Content-Type: application/json
    Host: blog.example.com

    Similarly, in this example, all comments associated with post #2 get deleted.

    Delete /posts/2/comments HTTP/1.1
    Content-Length: 0
    Content-Type: application/json
    Host: blog.example.com
    Because DELETE method modifies the state of the system, it is not considered to be safe. However, the
    DELETE method is considered idempotent; subsequent DELETE requests would still leave the resource and
    the system in the same state.

    Put:

    The PUT method allows a client to modify a resource state. A client modifies the state of a resource and
    sends the updated representation to the server using a PUT method. On receiving the request, the server
    replaces the resource’s state with the new state.

    In this example, we are sending a PUT request to update a post identified by 1. The request contains
    an updated blog post’s body along with all of the other fields that make up the blog post. The server,
    on successful processing, would return a status code 200, indicating that the request was processed
    successfully.

    Consider the case in which we just wanted to update the blog post title. The HTTP semantics dictate
    that as part of the PUT request we send the full resource representation, which includes the updated title as
    well as other attributes such as blog post body and so on that didn’t change. However, this approach would
    require that the client has the complete resource representation, which might not be possible if the resource
    is very big or has a lot of relationships. Additionally, this would require higher bandwidth for data transfers.
    So, for practical reasons, it is acceptable to design your API that tends to accept partial representations as
    part of a PUT request.

    Clients can also use PUT method to create a new resource. However, it will only be possible when the
    client knows the URI of the new resource. In a blogging application, for example, a client can upload an
    image associated with a blog post. In that scenario, the client decides the URL for the image as shown in this
    example:

    PUT http://blog.example.com/posts/1/images/author.jpg

    PUT is not a safe operation, as it changes the system state. However, it is considered idempotent, as
    putting the same resource once or more than once would produce the same result.

    Post:

    The POST method is used to create resources. Typically, it is used to create resources under subcollections—
    resource collections that exist under a parent resource. For example, the POST method can be used to create
    a new blog entry in a blogging application. Here, “posts” is a subcollection of blog post resources that reside
    under a blog parent resource.

    Unlike PUT, a POST request doesn’t need to know the URI of the resource. The server is responsible
    for assigning an ID to the resource and deciding the URI where the resource is going to reside. In the
    previous example, the blogging application will process the POST request and create a new resource under
    http://blog.example.com/posts/12345, where “12345” is the server generated id. The Location header in
    the response contains the URL of the newly created resource.

    The POST method is very flexible and is often used when no other HTTP method seems appropriate.
    Consider the scenario in which you would like to generate a thumbnail for a JPEG or PNG image. Here we
    ask the server to perform an action on the image binary data that we are submitting. HTTP methods such as
    GET and PUT don’t really fit here, as we are dealing with an RPC-style operation. Such scenarios are handled
    using the POST method.

    The POST method is not considered safe, as it changes system state. Also, multiple POST invocations
    would result in multiple resources being generated, making it nonidempotent.

    Patch:

    As we discussed earlier, the HTTP specification requires the client to send the entire resource representation
    as part of a PUT request. The PATCH method proposed as part of RFC 5789 (http://tools.ietf.org/html/
    rfc5789) is used to perform partial resource updates. It is neither safe nor idempotent. Here is an example
    that uses PATCH method to update a blog post title.

    HTTP Status Codes:

    •	 Informational Codes—Status codes indicating that the server has received the
    request but hasn’t completed processing it. These intermediate response codes are
    in the 100 series.
    •	 Success Codes—Status codes indicating that the request has been successfully
    received and processed. These codes are in the 200 series.
    •	 Redirection Codes—Status codes indicating that the request has been processed, but
    the client must perform an additional action to complete the request. These actions
    typically involve redirecting to a different location to get the resource. These codes
    are in the 300 series.
    •	 Client Error Codes—Status codes indicating that there was an error or a problem
    with client’s request. These codes are in the 400 series.
    •	 Server Error Codes—Status codes indicating that there was an error on the server
    while processing the client’s request. These codes are in the 500 series.
     */
}
