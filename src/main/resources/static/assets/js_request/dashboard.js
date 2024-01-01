$(function () {
    var start = moment().subtract(29, 'days');
    var end = moment();
    function cb(start, end) {
        $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        getData(start.format('MMMM D, YYYY'), end.format('MMMM D, YYYY'), $('#branch-select').val())
        // var token = $("meta[name='_csrf']").attr("content");
        // var header = $("meta[name='_csrf_header']").attr("content");
    }
    $('#reportrange').daterangepicker({
        startDate: start,
        endDate: end,
        ranges: {
            'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }
    }, cb);
    cb(start, end);
});

$('#branch-select').on('change', function () {
    let dateRangeString = $('#reportrange span').html();
    let dateRangeArray = dateRangeString.split(" - ");
    let startDateString = dateRangeArray[0];
    let endDateString = dateRangeArray[1];
    $('#total-sales').html("0")
    $('#product-sold').html("0")
    $('#orders-sold').html("0")
    $('#profit').html("0")
    var tableBody = document.getElementById('table-order-report');
    tableBody.innerHTML = ''
    tableBody = document.getElementById('table-product-report');
    tableBody.innerHTML = ''
    getData(startDateString, endDateString, $('#branch-select').val())
});

var currentRequest = null;
function getData(start, end, branch) {
    // function poll() {
    //     if (currentRequest) {
    //         currentRequest.abort();
    //     }
        currentRequest = $.ajax({
            type: 'POST',
            url: '/',
            data: { startDate: start, endDate: end, branch: branch },
            success: function (data) {

                $('#total-sales').html('$' + data.totalAmount);
                $('#product-sold').html(data.totalProduct);
                $('#orders-sold').html(data.orderList.length);
                $('#profit').html('$' + data.profit);
                showOrder(data.orderList);
                showProduct(data.productList);

                currentRequest = null;
                // poll();
            },
            error: function () {
                currentRequest = null;
            }
        });
    // }
    // poll();
}

var stompClient = null;

function connect() {
    var socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    var headers = {
        'Origin': 'http://localhost:8080'
    };

    stompClient.connect(headers, function (frame) {
        stompClient.subscribe('/topic/quantity-updates', function (response) {
            var body = JSON.parse(response.body);
            console.log(body.oldQuantity);
            console.log(body.productId);
            console.log(body.quantity);
            console.log(body.branch);
            if( $('#branch-select').val() === "All"){
                old = $(`#${body.productId}`).val()
                $(`#${body.productId}`).val( old - body.oldQuantity + body.quantity)
            }
            if( $('#branch-select').val() === body.branch){
                $(`#${body.productId}`).val( body.quantity)
            }
        });
    });

}
connect();
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}
function showOrder(orderList) {
    var tableBody = document.getElementById('table-order-report');
    tableBody.innerHTML = ''
    orderList.forEach( (order) => {
        var newRow = document.createElement('tr');
        var createdAtDate = new Date(order.createdAt);

        var formattedDate = createdAtDate.toISOString().split('T')[0];
        var totalQuantity = order.orderItems.reduce((acc, item) => acc + item.quantity, 0);

        newRow.innerHTML = `
                                        <td>${order.id}</td>
                                        <td>${order.nameOfCustomer}</td>
                                        <td>${order.totalAmount}</td>
                                        <td>${order.cash}</td>
                                        <td>${(order.cash!==0)?(order.cash - order.totalAmount):0}</td>
                                        <td>${formattedDate}</td>
                                        <td>${totalQuantity}</td>
                                        `;
        tableBody.appendChild(newRow);
    })
}

function showProduct(productList){
    var tableBody = document.getElementById('table-product-report');
    tableBody.innerHTML = ''
    productList.forEach( (product) => {
        var newRow = document.createElement('tr');
        var readonly = '';
        if($('#branch-select').val() === 'All'){
            readonly = 'readonly'
        }
        newRow.innerHTML = `
                                        <td><div class="media">
                                            <img src="data:image/png;base64,${product.image}"
                                                 class="align-self-center mr-3 avatar-40 img-fluid rounded"
                                                 alt="#">
                                            <div class="media-body">
                                                <div
                                                        class="d-flex flex-wrap justify-content-start align-items-center">
                                                    <p class="mb-0 font-weight"><strong>Name:</strong> ${product.name}<br/><strong>Code:</strong> ${product.barCode}</p>
                                                </div>
                                            </div>
                                        </div></td>
                                        <td>${product.brand.name}</td>
                                        <td>${product.importPrice}</td>
                                        <td>${product.retailPrice}</td>
                                        <td> 
                                            <input ${readonly} value="${product.quantityOfBranch}" id="${product.id}"  type="text" class="quantityOfProduct form-control d-flex align-items-center" aria-label="Default" aria-describedby="inputGroup-sizing-default">
                                        </td>
                                        `;
        var quantityInput = newRow.querySelector('.quantityOfProduct');
        quantityInput.addEventListener('change', function() {
            const productId = product.id;
            const quantity = this.value;
            console.log(productId)
            console.log(quantity)
            $.ajax({
                type: 'POST',
                url: '/product/update-quantity',
                data: { productId: productId, quantity: quantity, branch:$('#branch-select').val() },
                success: function (data) {
                    createToast("success", product.name + " was updated to " + quantity + " items")
                },
                error: function () {
                }
            });
        });
        tableBody.appendChild(newRow);
    });
}



$('#branch-select-product').on('change', function () {
    console.log($('#branch-select-product').val())
    $.ajax({
        type: 'POST',
        url: '/product/get-by-branch',
        data: { branch: $('#branch-select-product').val() },
        success: function (data) {
            console.log(data.productList)
            showProductList(data.productList);
        },
        error: function () {
        }
    });
});

function showProductList(productList){
    var table = document.getElementById('datatable-1');

    var rows = table.getElementsByTagName('tr');

    var headerRow = rows[0];
    while (headerRow.getElementsByTagName('th').length > 0) {
        headerRow.removeChild(headerRow.getElementsByTagName('th')[0]);
    }

    var headers = ['Product', 'Brand', 'Import Price', 'Retail Price', 'Quantity'];
    for (var i = 0; i < headers.length; i++) {
        var th = document.createElement('th');
        th.innerHTML = headers[i];
        headerRow.appendChild(th);
    }

    tableBody = document.getElementById('table-product-list');
    tableBody.innerHTML = '';

    productList.forEach( (product) => {
        var newRow = document.createElement('tr');
        var readonly = '';
        if($('#branch-select-product').val() === 'All'){
            readonly = 'readonly'
        }
        newRow.innerHTML = `
                                        <td><div class="media">
                                            <img src="data:image/png;base64,${product.image}"
                                                 class="align-self-center mr-3 avatar-40 img-fluid rounded"
                                                 alt="#">
                                            <div class="media-body">
                                                <div
                                                        class="d-flex flex-wrap justify-content-start align-items-center">
                                                    <p class="mb-0 font-weight"><strong>Name:</strong> ${product.name}<br/><strong>Code:</strong> ${product.barCode}</p>
                                                </div>
                                            </div>
                                        </div></td>
                                        <td>${product.brand.name}</td>
                                        <td id="${product.id + '-importPrice'}">${product.importPrice}</td>
                                        <td id="${product.id + '-retailPrice'}">${product.retailPrice}</td>
                                        <td>
                                            <input ${readonly} value="${product.quantityOfBranch}" id="${product.id}"  type="text" class="quantityOfProduct form-control d-flex align-items-center" aria-label="Default" aria-describedby="inputGroup-sizing-default">
                                        </td>
                                        `;
        var quantityInput = newRow.querySelector('.quantityOfProduct');
        quantityInput.addEventListener('change', function() {
            const productId = product.id;
            const quantity = this.value;
            console.log(productId)
            console.log(quantity)
            console.log($('#branch-select-product').val())
            $.ajax({
                type: 'POST',
                url: '/product/update-quantity',
                data: { productId: productId, quantity: quantity, branch:$('#branch-select-product').val() },
                success: function (data) {
                    createToast("success", product.name + " was updated to " + quantity + " items")
                },
                error: function () {
                }
            });
        });
        tableBody.appendChild(newRow);
    });
}