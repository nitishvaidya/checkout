Run:
 Need maven to build this project
 command: mvn clean install
 
To run the jar: java -jar target/checkout-0.0.1-SNAPSHOT.jar

It will run on port 8080

Assumptions made in this project:
1. Currency is not included as it is assumed every price is in same currency and upto 2 decimal places.
2. Discounts and deals have validity specified. They wont be applied to basket if they are expired.
3. At any time only one active discount and one active deal is applied to basket. It will be based on the quantity of product during checkout.
4. There is only one basket active at a time. New basket will be created only when you add product to it. Once it is checked out only then new basket is created.
5. Discount are in format of buy 2 get 20% off. You can only add percentage on number of items you want to give discount on.
6. Product,  Discounts and deals are stored independent of each other. Removing product when discount and deal is existing for that product has no effect.



Use Postman or swagger to call API's
For swagger open url : http://localhost:8080/swagger-ui.html to view list of all api's and call them. 
However for model what data to send, please check this file for each API as some of models are reused.

___________________________________________________________________________________________________________________________________________________________________
1. API specifications for Store owner to manage product inventory, discounts and deals:
___________________________________________________________________________________________________________________________________________________________________

Products:
___________________________________________________________________________________________________________________________________________________________________
Create product: POST /v1/products
Assumptions:
1. Name of product is unique and is case insensitive. 
For example you cannot create two products with name laptop, if you want to add differnt type of laptop , update description and add name as laptop.

Sample Request: 

{
  "description": "dell mouse",
  "name": "mouse",
  "price": 500,
  "quantity": 10
}

Response status : 201
Sample response : "5c1f0881-880f-4a03-9a02-78de6d7a037b" (id of product)


Get All products: GET /v1/products
Sample response: 

[
  {
    "id": "74a15bba-e003-4978-af32-70c0b435a5aa",
    "name": "laptop",
    "description": "laptop value",
    "price": 5000,
    "quantity": 8
  },
  {
    "id": "5c1f0881-880f-4a03-9a02-78de6d7a037b",
    "name": "mouse",
    "description": "dell mouse",
    "price": 500,
    "quantity": 5
  }
]

Response status : 200

Update product: PUT /v1/products/{id}
Assumptions:
1. Since it is PUT, you need to enter description, price and quantity even if it is not changed as it is using same validation as create.

{id} is uuid of existing product.
Sample Request body: 

{
  "description": "dell mouse",
  "price": 500,
  "quantity": 10
}

Response status : 204

Delete product: DELETE /v1/products/{id}
Assumptions:
{id} is uuid of existing product.

Response status : 204
___________________________________________________________________________________________________________________________________________________________________

Discounts:
___________________________________________________________________________________________________________________________________________________________________

Create product: POST /v1/products/{productId}/discounts
Assumptions:
Discount are in format of buy 2 get 20% off. You can only add percentage on number of items you want to give discount on.

{productId} is uuid of existing product.

Sample Request: 

{
  "applicableTill": "2020-08-09T04:00:05.268Z",
  "buyQuantity": 2,
  "description": "buy 2 get 10% off",
  "percentage": 10
}

Response status : 201
Sample response : "8c1f0881-880f-4a03-9a02-78de6d7a037b" (id of discount)

Get all active discounts as of now: GET /v1/products/{productId}/discounts
Assumptions: It only give active discounts irrespective of product was removed or not.
Sample response: 

[
  {
    "id": "ef79aab0-07f8-4042-8b78-0e47854f4de9",
    "description": "buy 2 get 10% off",
    "buyQuantity": 2,
    "percentage": 10,
    "applicableTill": "2020-08-11T04:00:05.268"
  }
]

Response status : 200

___________________________________________________________________________________________________________________________________________________________________

Deals:
___________________________________________________________________________________________________________________________________________________________________

Create product: POST /v1/products/{productId}/deals

{productId} is uuid of existing product.

Sample Request: 

{
  "applicableTill": "2020-08-09T03:58:51.578Z",
  "buyQuantity": 2,
  "description": "buy 2 get 1 mouse and 1 bag",
  "freeProducts": {
    "5c1f0881-880f-4a03-9a02-78de6d7a037b": 1,
    "6c1f0881-880f-4a03-9a02-78de6d7a037b": 2
  }
}

Response status : 201
Sample response : "7c1f0881-880f-4a03-9a02-78de6d7a037b" (id of deal)


Get all active deals as of now: GET /v1/products/{productId}/deals
Assumptions: It only give active deals irrespective of product was removed or not.
Sample response: 

[
  {
    "id": "36e0aa49-0679-45c9-b53e-1e014060c17a",
    "description": "buy 2 get 1 mouse and 1 bag",
    "buyQuantity": 2,
    "freeProducts": {
      "5c1f0881-880f-4a03-9a02-78de6d7a037b": 1,
      "6c1f0881-880f-4a03-9a02-78de6d7a037b": 2
    },
    "applicableTill": "2020-08-10T03:58:51.578"
  }
]

Response status : 200


___________________________________________________________________________________________________________________________________________________________________
2. API specifications for Store owner to checkout product:
___________________________________________________________________________________________________________________________________________________________________

Get my basket:GET /v1/baskets/me
___________________________________________________________________________________________________________________________________________________________________
Assumptions: 
1. If basket is not created it will throw error. Basket is created only when a product is added to basket.
Once basket is checked out, you have to add product to basket to create a new one.
2. When getting basket details, it only shows the products you have added.

Sample response: 
{
  "products": [
    {
      "name": "laptop",
      "quantity": 4
    }
  ],
  "discounts": null,
  "deals": null,
  "totalPrice": null
}


___________________________________________________________________________________________________________________________________________________________________

Add / Update product to my basket :PUT /v1/baskets/me/v1/products/{id}
___________________________________________________________________________________________________________________________________________________________________
Assumptions: 
1. If there is no basket, it will create new basket and add the product to it.
2. It will also remove the added quantity from our product inventory as well. 
3. Once product is added here, even if it is removed from inventory, it will sit with this basket.

{id} is existing product id

Sample request body:
{
  "quantity": 4
}

Sample response : 204


___________________________________________________________________________________________________________________________________________________________________

Remove product from my basket :DELETE /v1/baskets/me/v1/products/{id}
___________________________________________________________________________________________________________________________________________________________________
Assumptions: 
1. It will also add the quantity of this product to inventory as well. 

{id} is existing product id in basket

Sample response : 204

___________________________________________________________________________________________________________________________________________________________________

Checkout my basket :POST /v1/baskets/me/checkout
___________________________________________________________________________________________________________________________________________________________________
Assumptions: 
1. It will provide you summary of what discount / deal applied for this basket

Sample response : 
{
  "products": [
    {
      "name": "laptop",
      "quantity": 4
    }
  ],
  "discounts": [
    "buy 2 get 10% off"
  ],
  "deals": [
    {
      "name": "buy 2 get 1 mouse and 1 bag",
      "products": [
        {
          "name": "mouse",
          "quantity": 1
        }
      ]
    }
  ],
  "totalPrice": 19000
}


