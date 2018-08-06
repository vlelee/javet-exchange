var refreshStockPrice;
var tradeHistoryInterval;
var tradeHistoryGraph = null;
var currentTradeCount = 0;


$(document).ready(function () {
    loadStrategies();
});

function updateInvestmentValue() {
    quantity = $("#strategy-quantity-input").val();
    stock = $("#strategy-share-select option:selected").text();
    let stock_price_regex = /.+\(\$(\d+\.\d+)\)/g
    stock_vals = stock_price_regex.exec(stock);
    if (quantity && stock) {
        let stock_price = parseFloat(stock_vals[1]).toFixed(2);
        let investment_value = (stock_price * quantity).toFixed(2)
        $("#new-investment-value").val(investment_value);
    }
}

function loadStrategies() {
    $.get("/api/strategies", function (strategies) {
        //${strategy.stock.ticker == 'C' ? '<img src="https://botw-pd.s3.amazonaws.com/styles/logo-thumbnail/s3/112011/city_bank_logo.png?itok=dFZm2BBW" width="30px" height="30px" />' : strategy.stock.ticker}
        $("#strategy-info-tbody").html("");
        
        $.get("templates/strategies-list.mustache", function(strategies_template) {
            var active_strategies = strategies.filter(strategies => strategies.active == true);
            Mustache.parse(strategies_template);   // optional, speeds up future uses
            var strategies_rendered = Mustache.render(strategies_template, 
                                                      {active_strategies: active_strategies, 
                                                       inactive_strategies: strategies.filter(strategies => strategies.active == false)});
            $("#strategies-list").html(strategies_rendered);    
            $.each(strategies, function(index, strategy) {
                /*
                let start_char = strategy.strategyProfitString.charAt(0);
                let text_class = (start_char == "+") ? "text-success" : ((start_char == "-") ? "text-danger" : "text-info");
                $(`#strategy${strategy.id}-profit`).addClass(text_class)   */
                
                $(`#strategy${strategy.id}-profit`).text(strategy.strategyProfitString)
                let table_row_class = (strategy.strategyProfitString.charAt(0) == "+") ? "table-success" : ((strategy.strategyProfitString.charAt(0) == "-") ? "table-danger" : "table-info");
                $(`#strategy${strategy.id}-row`).removeClass("table-danger table-info table-success").addClass(table_row_class)
            });
            
            $.each(active_strategies, function(index, strategy) {
                setInterval(function () {
                    $.get(`/api/strategies/${strategy.id}/profit`, function (strategy_profit) {
                        //let text_class = (strategy_profit.charAt(0) == "+") ? "text-success" : ((strategy_profit.charAt(0) == "-") ? "text-danger" : "text-info");
                        //$(`#strategy${strategy.id}-profit`).text(strategy_profit).removeClass("text-danger text-info text-success").addClass(text_class)
                        $(`#strategy${strategy.id}-profit`).text(strategy_profit)
                        let table_row_class = (strategy_profit.charAt(0) == "+") ? "table-success" : ((strategy_profit.charAt(0) == "-") ? "table-danger" : "table-info");
                        $(`#strategy${strategy.id}-row`).removeClass("table-danger table-info table-success").addClass(table_row_class)
                    });
                    $.get(`/api/strategies/${strategy.id}/position`, function (strategy_position) {
                        $(`#strategy${strategy.id}-next-position`).text(strategy_position)
                    });
                    $.get(`/api/strategies/${strategy.id}`, function (strategy) {
                        if (!strategy.active)
                            window.location.reload(true);
                    });
                }, 5000);
            })
        });
    });
}

function displayStrategyHistory(strategy_id, strategy_name, active = false) {
    clearInterval(tradeHistoryInterval);
    tradeHistoryGraph = null;
    
    loadTradeHistory(strategy_id, strategy_name);
        
    if (active) {
        tradeHistoryInterval = setInterval(function () {
            refreshTradeHistory(strategy_id, strategy_name)
        }, 5000);
    }
}

function loadTradeHistory(strategy_id, strategy_name) {
    currentTradeCount = 0;
    
    $.get(`/api/strategies/${strategy_id}/trades`, function (trades) {
        $.each(trades, function (index, trade) {
            trade.index = index + 1;
            trade.timestamp = new Date(trade.timeTraded).toUTCString();
            trade.tradePrice = trade.tradePrice.toFixed(2);
            trade.buyingString = trade.buying ? 'Buying' : 'Selling'
            trade.invValue = (trade.numShares * trade.tradePrice).toFixed(2);
        });        
        trades.reverse();
        
        $.get("/templates/strategy-history.mustache", function(history_template) {
            Mustache.parse(history_template);   // optional, speeds up future uses
            var strategies_rendered = Mustache.render(history_template,  {strategy_name: strategy_name, trades: trades});
            $("#stocks-pane").slideUp('slow');
            $("#strategy-detail-pane").html(strategies_rendered).slideDown('slow');
            
            if(trades.length > 0) loadNewTradeHistoryGraph(strategy_id);
        });
    });
}


function loadNewTradeHistoryGraph(strategy_id) {
    $.get(`/api/strategies/${strategy_id}/trade_evals`, function (tradeVals) {
            
        if(tradeVals.length > 0) {
            currentTradeCount = tradeVals.length;
            $("#strategy-trade-history-graph").show();
            
            currentTradeCount = tradeVals.length;
            let tradeIndices = Array.apply(null, {length: currentTradeCount}).map(Number.call, Number)
            var ctx = document.getElementById("strategy-trade-history-graph").getContext('2d');
            tradeHistoryGraph = new Chart(ctx, {
                type: 'line',
                options:
                    {
                        scales: {
                            yAxes: [{
                                scaleLabel: {
                                    display: true,
                                    labelString: "Investment"
                                }
                            }],
                            xAxes: [{
                                scaleLabel: {
                                    display: true,
                                    labelString: "Trade No."
                                }
                            }]
                        }
                    },
                data: {
                    labels: tradeIndices,
                    datasets: [{
                        label: "Investment Value at Each Trade",
                        data: tradeVals,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                }
            });
        }
    });    
}

function refreshTradeHistory(strategy_id, strategy_name) {
    loadTradeHistory(strategy_id, strategy_name);
    $.get(`/api/strategies/${strategy_id}/trade_evals`, function (tradeVals) {
        if(tradeVals.length > 0) {
            if(tradeHistoryGraph) {
                if(currentTradeCount < tradeVals.length) {
                    currentTradeCount = tradeVals.length;
                    let tradeIndices = Array.apply(null, {length: tradeVals.length}).map(Number.call, Number)
                    tradeHistoryGraph.labels = tradeIndices;
                    tradeHistoryGraph.data = tradeVals;
                    tradeHistoryGraph.update();      
                }          
            } else {
                loadNewTradeHistoryGraph(strategy_id);
            }
        }
    });
}


function openCreateNewStrategyModal() {
    $.get("templates/strategy-form.mustache", function (template) {
        Mustache.parse(template);   // optional, speeds up future uses
        var rendered = Mustache.render(template);
        clearGlobalModal();
        $("#global-modal-title").text("Create New Strategy")
        $('#global-modal-body').html(rendered);


        $("#strategy-quantity-input").change(updateInvestmentValue);
        $("#strategy-share-select").change(updateInvestmentValue);
        $("#strategy-share-select").val($("#strategy-share-select option:first").val());


        // TODO: remove extraneous stock-list.json fields and create a combined field for symbol and name to allow searching for name and symbol.
        var options_fullname =
            {
                url: "res/stock-list.json", getValue: "Symbol",
                list: {
                    maxNumberOfElements: 10,
                    match: {enabled: true},
                    onSelectItemEvent: function () {
                        newStrategySelectStock($("#strategy-stock-input").getSelectedItemData().Symbol, $("#strategy-stock-input").getSelectedItemData().Name)
                    },
                    onKeyEnterEvent: function () {
                        newStrategySelectStock($("#strategy-stock-input").getSelectedItemData().Symbol, $("#strategy-stock-input").getSelectedItemData().Name)
                    }
                },

                template: {
                    type: "description",
                    fields: {
                        description: "Name"
                    }
                }
            };
        $("#new-investment-value").data("stock-base-price", 0)
        $("#strategy-stock-input").easyAutocomplete(options_fullname).css("min-width", "300px");
        $(".easy-autocomplete-container").css("min-width", "300px");
        $("#strategy-quantity-input").on('input', function () {
            let stock_base_price = $("#new-investment-value").data("stock-base-price")
            if (stock_base_price)
                $("#new-investment-value").val($("#strategy-quantity-input").val() * stock_base_price.toFixed(2));
        });

        $("#global-modal-footer").html(`
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
            <button type="button" class="btn btn-success" onClick="createStrategy()">Create Strategy</button>
        `);
        $("#new-strategy-warnings").hide();
        $("#global-modal").modal();

    });
}

function newStrategySelectStock(ticker, name) {
    clearInterval(refreshStockPrice);
    $("#strategy-stock-input").data("Symbol", ticker);
    $("#strategy-stock-input").val(name);
    refreshStockPrice = setInterval(function () {
        $.get(`/api/stockprices/${ticker}/latest`, function (price) {
            $("#new-investment-value").data("stock-base-price", parseFloat(price));
            $("#new-investment-value").val($("#strategy-quantity-input").val() * parseFloat(price).toFixed(2));
        });

    }, 3000);
}


function openEditStrategyModal(strategy_id) {
    $.get("templates/strategy-edit-form.mustache", function (template) {
        Mustache.parse(template);   // optional, speeds up future uses
        clearGlobalModal();
        $("#global-modal-title").text("Edit Active Strategy")
        $.get("/api/strategies/" + strategy_id, function (strategy) {
            var strategy_algo_dict = {"TMA": "Two-Moving Averages", "PB": "Price Breakout", "BB": "Bollinger Bands"}

            var rendered = Mustache.render(template, {
                strategyName: strategy.strategyName, strategyType: strategy_algo_dict[strategy.algo],
                startingPosition: strategy.startingPosition, strategyStock: strategy.stock.ticker,
                stockQuantity: strategy.numShares, lossExitThreshold: strategy.exitThresholdHigh,
                gainExitThreshold: strategy.exitThresholdLow
            });
            $('#global-modal-body').html(rendered);
            $("#new-strategy-warnings").hide();
            $("#global-modal-footer").html(`
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" onClick="updateStrategy(${strategy_id})">Update Strategy</button>
            `);
            $("#global-modal").modal();
        });
    });
}

function updateStrategy(strategy_id) {
    let strategy_name = $("#strategy-name-input").val();
    let gain_threshold_exit = parseFloat($("#gain-exit-threshold-input").val()).toFixed(2);
    let loss_threshold_exit = parseFloat($("#loss-exit-threshold-input").val()).toFixed(2);
    let updated_strategy = {
        "strategyName": strategy_name,
        "exitThresholdHigh": gain_threshold_exit,
        "exitThresholdLow": loss_threshold_exit
    };
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        url: `/api/strategies/${strategy_id}`,
        method: "PUT",
        data: JSON.stringify(updated_strategy),
        success: function () {
            window.location.reload(true);
        }
    });
}

function openEndStrategyModal(strategy_id) {
    clearGlobalModal();
    $("#global-modal-title").text("End Active Strategy")
    $('#global-modal-body').html("Are you sure you would like to end this strategy now?");
    $("#global-modal-footer").html(`
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-danger" onClick="endStrategy(${strategy_id})">End Strategy</button>
    `);
    $("#global-modal").modal();
}

function endStrategy(strategy_id) {
    $.ajax({
        url: `/api/strategies/${strategy_id}/deactivate`,
        method: "PUT",
        success: function () {
            window.location.reload(true);
        }
    });
}



// This is called once a user clicks the 'Create Strategy' button. This function which will actually call the REST service to create the strategy.
function createStrategy() {
    $("#new-strategy-warnings").hide();
    let strategy_name = $("#strategy-name-input").val();
    let strategy_algo = $("#strategy-type-select").val();
    let strategy_share_quantity = parseInt($("#strategy-quantity-input").val());
    let strategy_start = new Date().toISOString(); // Taken from https://stackoverflow.com/questions/5129624/convert-js-date-time-to-mysql-datetime

    let gain_threshold_exit = parseFloat($("#gain-exit-threshold-input").val()).toFixed(2);
    let loss_threshold_exit = parseFloat($("#loss-exit-threshold-input").val()).toFixed(2);

    if (!strategy_name) {
        createStrategyWarning("Missing Name!", "Please enter a name to identify your strategy!")
        return
    } else if (!strategy_share_quantity) {
        createStrategyWarning("Missing Quantity!", "Please enter a quantity of stock you'd like to begin your investment with!")
        return
    } else if (strategy_share_quantity < 1) {
        createStrategyWarning("Invalid Share Quantity!", "Please enter a quantity of stock you'd like to begin your investment with!")
        return
    }

    // TODO: Resolve Stock Name TBD
    // TODO: Fixed threshold rounding issue
    // TODO: Fix initiation price
    let new_stock = {
        "ticker": $("#strategy-stock-input").data("Symbol"),
        "stockName": $("#strategy-stock-input").val()
    };
    let new_strategy = {
        "strategyName": strategy_name,
        "algo": strategy_algo,
        "initiallyBuying": ($("#strategy-starting-position-select").val() == "Buying"),
        "stock": new_stock,
        "startTime": strategy_start,
        "initiationPrice": $("#new-investment-value").data("stock-base-price"),
        "numShares": strategy_share_quantity,
        "exitThresholdHigh": gain_threshold_exit,
        "exitThresholdLow": loss_threshold_exit,
        "active": true
    };
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        url: "/api/strategies",
        method: "POST",
        data: JSON.stringify(new_strategy),
        success: function (obj) {
            window.location.reload(true);
        }
    });
}
