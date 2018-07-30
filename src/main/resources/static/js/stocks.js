$(document).ready(function() {
    findAndLoadRelevantStocks();
    
    var options_fullname = 
        { 
            url: "res/stock-list.json", 
            getValue: "Name", 
            list: { 
                match: {
                    enabled: true
                },
                onSelectItemEvent: function() {
                    $("#new-share-ticker-input").val($("#new-share-name-input").getSelectedItemData().Symbol);
                },
                onKeyEnterEvent: function() {
                    $("#new-share-ticker-input").val($("#new-share-name-input").getSelectedItemData().Symbol);
                }
            }
        };
    $("#new-share-name-input").easyAutocomplete(options_fullname);
    
    
    var options_ticker = 
        { 
            url: "res/stock-list.json", 
            getValue: "Symbol", 
            list: { 
                match: {
                    enabled: true
                },
                onSelectItemEvent: function() {
                    $("#new-share-name-input").val($("#new-share-ticker-input").getSelectedItemData().Name);
                }
            }
        };
    
    $("#new-share-ticker-input").easyAutocomplete(options_ticker);
    
    // If one of the inputs are emptied, empty the other one.
    $("#new-share-ticker-input").keyup(function() { $("#new-share-name-input").val("") });
    $("#new-share-name-input").keyup(function() { $("#new-share-ticker-input").val("") });

});

function findAndLoadRelevantStocks() {
    $.get("http://localhost:8082/stocks", function(data) {
        let tracked_stocks = []
        $.each(data, function(index, stock) {
            console.log(stock.stockName);
            if(stock.tracking) {tracked_stocks.push(stock.ticker);}

        })
        $.each(tracked_stocks, function(index, stock) {
            let get_relevant_stocks_url = "https://api.iextrading.com/1.0/stock/" + stock + "/relevant";
            $.get(get_relevant_stocks_url, function(relevant_stocks) {
                loadStockPrice(stock, relevant_stocks["symbols"]);
            });
        }); 

    });
}

function loadStockPrice(stock, relevant_stocks) {
    let get_stock_price_url = "https://api.iextrading.com/1.0/stock/" + stock + "/ohlc";
    $.get(get_stock_price_url, function(response) {
        open = response["open"];
        close = response["close"]
        $("#stock-info-tbody").append(`
            <tr>
                <th scope="row">${stock.toUpperCase()}</th>
                <td>${open.price}</td>
                <td>${close.price}</td>
                <td>${response.high}</td>
                <td>${response.low}</td>
                <td>
                    <button class="btn btn-xs m-0 p-0 text-info" onClick="showRelevant(${relevant_stocks})" style='background-color:transparent;'>
                        <i class="material-icons">more_horiz</i>
                    </button>
                    <button class="btn btn-xs m-0 p-0 text-warning" style='background-color:transparent;'>
                        <i class="material-icons">remove</i>
                    </button>
                </td>
            </tr>
        `);
    });
}


function findRelevantStocks(stock) {
    let get_relevant_stocks_url = "https://api.iextrading.com/1.0/stock/" + stock + "/relevant";
    const result = $.get(get_relevant_stocks_url);
    return result;     
}

function startTrackingStock() {
    let stock_name = $("#new-share-name-input").val();
    let stock_ticker = $("#new-share-ticker-input").val();
    let new_stock_to_track = {"ticker": stock_ticker, "stockName": stock_name, "tracking": "true"};
    console.log(new_stock_to_track)
    console.log(JSON.stringify(new_stock_to_track))
    $.ajax({
        
        headers: { 
            'Accept': 'application/json',
            'Content-Type': 'application/json' 
        },
        url: "http://localhost:8082/stocks", 
        method: "POST",
        data: JSON.stringify(new_stock_to_track), 
        success: function() {
            window.location.reload(true);
        }
    });
}
