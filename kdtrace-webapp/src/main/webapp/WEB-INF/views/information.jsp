<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Information</title>
    <style type="text/css">
        p{
            display: inline-block;
        }
    </style>
</head>
<body>

    <h3>Information Product</h3>
    <div>
        <div>
            <label>Product Name: </label>
            <p>${response.productModel.name}</p>
        </div>
        <div>
            <label>Product Type: </label>
            <p>${response.productModel.type}</p>
        </div>
        <div>
            <label>Product Unit: </label>
            <p>${response.productModel.unit}</p>
        </div>
        <div>
            <label>Product MFG: </label>
            <p>${response.productModel.mfg}</p>
        </div>
        <div>
            <label>Product EXP: </label>
            <p>${response.productModel.exp}</p>
        </div>
    </div>
    <h3>Information Producer</h3>
    <div>
        <div>
            <label> Company Name: </label>
            <p>${response.producerModel.companyName}</p>
        </div>
        <div>
            <label>Address: </label>
            <p>${response.producerModel.address}</p>
        </div>
        <div>
            <label>Email: </label>
            <p>${response.producerModel.email}</p>
        </div>
        <div>
            <label>Phone: </label>
            <p>${response.producerModel.phone}</p>
        </div>
    </div>
    <h3>Information Transport</h3>
    <div>
        <div>
            <label> Company Name: </label>
            <p>${response.transportModel.companyName}</p>
        </div>
        <div>
            <label>Address: </label>
            <p>${response.transportModel.address}</p>
        </div>
        <div>
            <label>Email: </label>
            <p>${response.transportModel.email}</p>
        </div>
        <div>
            <label>Phone: </label>
            <p>${response.transportModel.phone}</p>
        </div>
    </div>
    <h3>Information Delivery Car</h3>
    <div>
        <div>
            <label> Plate: </label>
            <p>${response.deliveryTruckModel.numberPlate}</p>
        </div>
        <div>
            <label>Type: </label>
            <p>${response.deliveryTruckModel.autoMaker}</p>
        </div>
        <div>
            <label>Delivery Time: </label>
            <p>${response.delivery_time}</p>
        </div>
        <div>
            <label>Receipt Time: </label>
            <p>${response.receipt_time}</p>
        </div>
    </div>
    <h3>Information Distributor</h3>
    <div>
        <div>
            <label> Company Name: </label>
            <p>${response.distributorModel.companyName}</p>
        </div>
        <div>
            <label>Address: </label>
            <p>${response.distributorModel.address}</p>
        </div>
        <div>
            <label>Email: </label>
            <p>${response.distributorModel.email}</p>
        </div>
        <div>
            <label>Phone: </label>
            <p>${response.distributorModel.phone}</p>
        </div>
    </div>
</body>
</html>