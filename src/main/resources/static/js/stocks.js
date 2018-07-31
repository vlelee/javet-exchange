var add_new_stock_template;

$(document).ready(function() {
    loadStocksWithPrices();
    
    $.get("templates/track-stock-modal.mustache", function(template) {
        Mustache.parse(template);   // optional, speeds up future uses
        add_new_stock_template = Mustache.render(template);
    });
    
});


// This function populates the  a modal to allow users to search for stocks to begin tracking. This can either be opened by clicking 'Track New Stock' at the 
// bottom of the stock list on the right-hand side of the page or by clicking 'Missing Stock?' within the strategy creation modal.
// This function uses an autocomplete library in conjunction with the res/stock-list.json file to help identify what stock the user would like to begin tracking.
// Dependencies: AutoComplete Library, Mustache Library, res/stock-list.json
function loadStocksWithPrices() {
    $.get("http://localhost:8082/api/stocks", function(data) {
        $.each(data, function(index, stock) {
            let get_stock_price_url = "https://api.iextrading.com/1.0/stock/" + stock.ticker.trim() + "/ohlc";
            $.get(get_stock_price_url, function(response) {
                open = response["open"];
                close = response["close"]
                $("#stock-info-tbody").append(`
                    <tr>
                        <th scope="row">${stock.ticker.toUpperCase()}</th>
                        <td>${response.open.price}</td>
                        <td>${response.close.price}</td>
                        <td>${response.high}</td>
                        <td>${response.low}</td>
                    </tr>
                `);
            });
        });
    });
}


// This function opens a modal to allow users to search for stocks to begin tracking. This can either be opened by clicking 'Track New Stock' at the 
// bottom of the stock list on the right-hand side of the page or by clicking 'Missing Stock?' within the strategy creation modal.
// This function uses an autocomplete library in conjunction with the res/stock-list.json file to help identify what stock the user would like to begin tracking.
// Dependencies: AutoComplete Library, Mustache Library, res/stock-list.json
function openTrackNewStockModal() {
    clearGlobalModal();
    $("#global-modal-title").text("Track New Stock");
    $('#global-modal-body').html(add_new_stock_template);

    // If one of the inputs are emptied, empty the other one.
    $("#new-share-ticker-input").keyup(function() { $("#new-share-name-input").val("") });
    $("#new-share-name-input").keyup(function() { $("#new-share-ticker-input").val("") });
    $("#global-modal-footer").html(`
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" onClick="startTrackingStock()">Track Stock</button>
    `);
    $("#global-modal").modal(); 
    
    
}

// This function is called from the modal created by openTrackNewStockModal(). This function calls the REST API and added a stock to the database.
// Tracked stocks can be used in strategies and will appear on the right-hand side of the page (unless hidden via toggleStockVisibility()).
// Dependencies: Local Stocks API, openTrackNewStockModal()
function startTrackingStock() { 
    let stock_name = $("#new-share-name-input").val();
    let stock_ticker = $("#new-share-ticker-input").val();
    let new_stock_to_track = {"ticker": stock_ticker, "stockName": stock_name, "tracking": "true"};
    $.ajax({       
        headers: { 
            'Accept': 'application/json',
            'Content-Type': 'application/json' 
        },
        url: "http://localhost:8082/api/stocks",
        method: "POST",
        data: JSON.stringify(new_stock_to_track), 
        success: function() {
            window.location.reload(true);
        }
    });
}