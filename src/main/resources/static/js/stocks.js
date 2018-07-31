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

function getPrice(ticker) {
    // TODO: Speak to the messenger service instead for this price.
    
}

function findAndLoadRelevantStocks() {
    $.get("http://localhost:8082/api/stocks", function(data) {
        $.each(data, function(index, stock) {
            loadStockPrice(stock.ticker, stock.tracking);
        });
    });
}


function toggleStockVisibility(ticker, visibility) {
    
    $.ajax({
        url: `http://localhost:8082/api/stocks/${ticker}/${visibility ? 'show' : 'hide'}`,
        method: "PUT",
        success: function() {
            window.location.reload(true);
        }
    });
    
}

function loadStockPrice(stock, tracked = false) {
    let get_stock_price_url = "https://api.iextrading.com/1.0/stock/" + stock.trim() + "/ohlc";
    $.get(get_stock_price_url, function(response) {
        open = response["open"];
        close = response["close"]
        stock_response = tracked ? "#stock-info-tracked" : "#stock-info-untracked" 
        
        toggle_stock_visibility_button = ""
        
        if(tracked) {
            toggle_stock_visibility_button = 
                `<button class="btn btn-xs m-0 p-0 text-warning" onClick="toggleStockVisibility('${stock.trim()}', false)" style='background-color:transparent;'>
                    <i class="material-icons">remove</i>
                </button>`;            
        } else {
            toggle_stock_visibility_button = 
                `<button class="btn btn-xs m-0 p-0 text-success" onClick="toggleStockVisibility('${stock.trim()}', true)" style='background-color:transparent;'>
                    <i class="material-icons">add</i>
                </button>`;            
        }
        
        
        $(stock_response).append(`
            <tr>
                <th scope="row">${stock.toUpperCase()}</th>
                <td>${open.price}</td>
                <td>${close.price}</td>
                <td>${response.high}</td>
                <td>${response.low}</td>
                <td>
                    ${toggle_stock_visibility_button}
                </td>
            </tr>
        `);
    });
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
        url: "http://localhost:8082/api/stocks",
        method: "POST",
        data: JSON.stringify(new_stock_to_track), 
        success: function() {
            window.location.reload(true);
        }
    });
}

function openTrackNewStockModal() {
    clearGlobalModal();
    $("#global-modal-title").text("Track New Stock")
    
    $.get("templates/track-stock-modal.mustache", function(template) {
        Mustache.parse(template);   // optional, speeds up future uses
        var rendered = Mustache.render(template);
        $('#global-modal-body').html(rendered);
    });
    $("#global-modal-footer").html(`
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" onClick="trackStockFromModal()">Track Stock</button>
    `);
    $("#global-modal").modal(); 
    
}
