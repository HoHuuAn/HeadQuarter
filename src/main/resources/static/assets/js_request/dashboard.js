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
    function poll() {
        if (currentRequest) {
            currentRequest.abort();
        }
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
                poll();
            },
            error: function () {
                currentRequest = null;
            }
        });
    }
    poll();
}

// var stompClient = null;
// function connect() {
//     var socket = new SockJS('/ws');
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, function (frame) {
//         stompClient.subscribe('/topic/stats', function (response) {
//             // const data = JSON.parse(response.body);
//             // console.log(response)
//             // Update the dashboard UI with the received updates
//             $('#total-sales').html('$' + response.totalAmount);
//             $('#product-sold').html(responsetotalProduct);
//             $('#orders-sold').html(response.orderList.length);
//             $('#profit').html('$' + response.profit);
//             showOrder(response.orderList);
//             showProduct(response.productList);
//         });
//     });
// }
// connect()
// function disconnect() {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
// }
//
// // Establish a WebSocket connection
// const socket = new WebSocket('ws://localhost:8081/socket-endpoint');
// // Handle incoming messages
// socket.onmessage = function (event) {
//     const data = JSON.parse(event.data);
//     // Update your UI with the received data
//     // For example:
//     $('#total-sales').html('$' + data.totalAmount);
//     $('#product-sold').html(data.totalProduct);
//     $('#orders-sold').html(data.orderList.length);
//     $('#profit').html('$' + data.profit);
//     showOrder(data.orderList);
//     showProduct(data.productList);
// };
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
                                        <td>${order.cash - order.totalAmount}</td>
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