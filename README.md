GET: http://localhost:8080/users/register
GET: http://localhost:8080/users
GET: http://localhost:8080/users/{id}
PUT: http://localhost:8080/users/{id}
DELETE: http://localhost:8080/users/{id}
GET: http://localhost:8080/users/my-info

GET: http://localhost:8080/auth/login
POST: http://localhost:8080/auth/introspect
POST: http://localhost:8080/auth/logout
POST: http://localhost:8080/auth/refresh
POST: http://localhost:8080/auth/outbound/authentication

POST: http://localhost:8080/cart
GET: http://localhost:8080/cart
GET: http://localhost:8080/cart/{cart_item_id}
GET: http://localhost:8080/cart/clear
DELETE: http://localhost:8080/cart

POST: http://localhost:8080/checkout
GET: http://localhost:8080/orders/{id}
GET: http://localhost:8080/orders
GET: http://localhost:8080/orders/user/{user_id}
PUT: http://localhost:8080//ordesr/{id}
DELETE: http://localhost:8080//orders/{id}

POST: http://localhost:8080/products
POST: http://localhost:8080/products/uploads/{id}
GET: http://localhost:8080/products
GET: http://localhost:8080/products/images/{image-name}
GET: http://localhost:8080/products/{id}
POST: http://localhost:8080/products/{id}
DELETE: http://localhost:8080/products/{id}

POST: http://localhost:8080/products/sizes/{id}
GET: http://localhost:8080/products/sizes
POST: http://localhost:8080/products/sizes/{productId}
PUT: http://localhost:8080/products/sizes/{id}
DELETE: http://localhost:8080/products/sizes/{id}

POST: http://localhost:8080/categories
GET: http://localhost:8080/categories
PUT: http://localhost:8080/categories/{id}
DELETE: http://localhost:8080/categories/{id}
