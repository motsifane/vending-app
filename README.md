# Vending Machine Frontend Application Documentation

## Overview

The Vending Machine Application is a Spring Boot application that simulates a vending machine. Users can view available items, add items to the cart, proceed to checkout, make payments, and receive change. The application uses an in-memory database to store vending items and leverages the Thymeleaf template engine for the front-end.

## Table of Contents

- [Features](#features)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)


## Features

- View available vending items with prices.
- Add items to the shopping cart.
- View the shopping cart contents and total price.
- Proceed to checkout and make payments.
- Calculate and display change or amount due after payment.

## Setup and Installation

1. Clone the repository: `git clone https://github.com/motsifane/vending-app.git`
2. Navigate to the project directory: `cd vending-app`
3. Build and run the application: `./mvnw spring-boot:run`

## Usage

1. Access the application by navigating to `http://localhost:8085` in your web browser.
2. View available items and their prices.
3. Click "Add to Cart" to add items to the cart.
4. Navigate to the cart to review the selected items and total price.
5. Proceed to checkout, enter payment amount, and see change or amount due.
6. Complete the purchase or clear the cart as needed.


