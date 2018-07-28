$(document).ready(function() {
    findAndLoadRelevantStocks();
});

async function findAndLoadRelevantStocks() {
    let user_stocks = await loadUserStocks();
    $.each(user_stocks, function(index, stock) {
        let relevant_stocks = findRelevantStocks(stock.toLowerCase());
        console.log(stock);
        console.log(relevant_stocks);
        loadStockPrice(stock, relevant_stocks);
        console.log(stock);
        console.log(relevant_stocks);
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

function loadUserStocks() {
    // TODO: Once the REST API has been implemented we'll actually go to our REST API for this information, for the time being we'll return a random few stocks.
    return ["AAPL", "AMZN", "C", "KO", "MSFT", "NFLX"];
}