$(document).ready(function() {
    findAndLoadRelevantStocks();
});

async function findAndLoadRelevantStocks() {
    let important_stocks = await findRelevantStocks("aapl");
    //important_stocks += findRelevantStocks("MSFT");
    //important_stocks += findRelevantStocks("GOOGL");
    //await sleep(2000);
    console.log(important_stocks)
    $.each(important_stocks["symbols"], function(index, stock) {
        console.log(stock);
            loadStockPrice(stock.toLowerCase());
    });     
}

function loadStockPrice(stock) {
    let get_stock_price_url = "https://api.iextrading.com/1.0/stock/" + stock + "/ohlc";
    $.get(get_stock_price_url, function(response) {
        open = response["open"];
        close = response["close"]
        $("#stock-info-tbody").append(`
            <tr>
                <td>${stock.toUpperCase()}</td>
                <td>${open.price}</td>
                <td>${close.price}</td>
                <td>${response.high}</td>
                <td>${response.low}</td>
            </tr>
        `);
    });
}


function findRelevantStocks(stock) {
    let get_relevant_stocks_url = "https://api.iextrading.com/1.0/stock/" + stock + "/relevant";
    const result = $.get(get_relevant_stocks_url); 
    
     return result;     
}