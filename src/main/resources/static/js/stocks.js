var add_new_stock_template;

$(document).ready(function() {
    loadStocksWithPricesLoop();
    
    $.get("templates/track-stock-modal.mustache", function(template) {
        Mustache.parse(template);   // optional, speeds up future uses
        add_new_stock_template = Mustache.render(template);
    });
    
});


// This function is called on page initialization and calls loadStocksWithPrices() in a loop to constantly update stock prices on the right-hand/bottom of the page.
// Dependencies: loadStocksWithPrices(), JAVET REST Services
function loadStocksWithPricesLoop() {
    loadStocksWithPrices();
    setInterval(loadStocksWithPrices
    , 5000);
}

// This function is called on page initialization by loadStocksWithPricesLoop() and calls the JAVET REST API to fetch all of the stocks in the DB which are all the stocks that the user has ever 
// created or used a strategy on. This data is populated into the right/bottom pane of the page.
// Dependencies: JAVET REST Services
function loadStocksWithPrices() {
        $.get("http://localhost:8082/api/stocks", function(stocks) {
            stocks.sort(function(a,b) { return ('' + a.attr).localeCompare(b.attr); });
            $.each(stocks, function(index, stock) {
                $.get(`http://localhost:8082/api/stockprices/${stock.ticker.trim()}/latest`, function(price) {
                    if($(`#stock-row-${stock.ticker.trim()}-price`).length == 0) {
                        $("#stock-info-tbody").append(`
                            <tr>
                                <th scope="row">${stock.ticker.toUpperCase()}</th>
                                <td id="stock-row-${stock.ticker.trim()}-price">${price}</td>
                            </tr>
                        `);
                    } else {
                        $(`#stock-row-${stock.ticker.trim()}-price`).text(price);
                    }
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