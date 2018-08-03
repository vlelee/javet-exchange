var refreshStockPrice;
var tradeHistoryInterval;
$(document).ready(function() {
    loadStrategies();
});

function updateInvestmentValue() {
    quantity = $("#strategy-quantity-input").val();
    stock = $("#strategy-share-select option:selected").text();
    let stock_price_regex = /.+\(\$(\d+\.\d+)\)/g
    stock_vals = stock_price_regex.exec(stock);
    if(quantity && stock) {
        let stock_price = parseFloat(stock_vals[1]).toFixed(2);
        let investment_value = (stock_price * quantity).toFixed(2)
        $("#new-investment-value").val(investment_value);
    }
}

function loadStrategies() {
    $.get("/api/strategies", function(data) {
        $("#strategy-info-tbody").html("");
        $.each(data, function(index, strategy) {
            strategy_location = (strategy.active) ? "#active-strategy-info-tbody" : "#inactive-strategy-info-tbody"
            
            $(strategy_location).append(`
                        <tr class="m-0 ${(strategy.active) ? "" : 'text-info'}">
                            <th scope="row">${strategy.strategyName}</th>
                            <td>${strategy.algo}</td>
                            <td>${strategy.stock.ticker == 'C' ? '<img src="https://botw-pd.s3.amazonaws.com/styles/logo-thumbnail/s3/112011/city_bank_logo.png?itok=dFZm2BBW" width="30px" height="30px" />' : strategy.stock.ticker}</td>
                            <td id='strategy${strategy.id}-profit'>-</td>
                            <td id='strategy${strategy.id}-next-position'>-</td>
                            <td id='strategy${strategy.id}-options' nowrap>
                                <button class="btn btn-xs m-0 p-0 text-primary" onClick="openStrategyHistoryModal(${strategy.id}, '${strategy.strategyName}', ${strategy.active})" style='background-color:transparent;'>
                                    <i class="material-icons">history</i>
                                </button>
                            </td>                            
                        </tr>
            `);
            if(strategy.active) {
                $(`#strategy${strategy.id}-options`).append(`
                                <button class="btn btn-xs m-0 p-0 text-success" onClick="openEditStrategyModal(${strategy.id})" style='background-color:transparent;'>
                                    <i class="material-icons">edit</i>
                                </button>
                                <button class="btn btn-xs m-0 p-0 text-danger" onClick="openEndStrategyModal(${strategy.id})" style='background-color:transparent;'>
                                    <i class="material-icons">remove_circle_outline</i>
                                </button>`);
            }
            $.get(`/api/strategies/${strategy.id}/profit`, function(strategy_profit) {
                let text_class = (strategy_profit.charAt(0) == "+") ? "text-success" :
                    ((strategy_profit.charAt(0) == "-") ? "text-danger" : "text-info");
                $(`#strategy${strategy.id}-profit`).text(strategy_profit).removeClass("text-danger text-info text-success").addClass(text_class)
            });
            $.get(`/api/strategies/${strategy.id}/position`, function(strategy_position) {
                $(`#strategy${strategy.id}-next-position`).text(strategy.active ? strategy_position : "Inactive")
            });
            if(strategy.active) {
                setInterval(function() {
                    $.get(`/api/strategies/${strategy.id}/profit`, function(strategy_profit) {
                        let text_class = (strategy_profit.charAt(0) == "+") ? "text-success" : 
                            ((strategy_profit.charAt(0) == "-") ? "text-danger" : "text-info");
                        $(`#strategy${strategy.id}-profit`).text(strategy_profit).removeClass("text-danger text-info text-success").addClass(text_class)
                    });
                    $.get(`/api/strategies/${strategy.id}/position`, function(strategy_position) {
                        $(`#strategy${strategy.id}-next-position`).text(strategy_position)
                    });
                    $.get(`/api/strategies/${strategy.id}`, function(strategy) {
                        if(!strategy.active)
                            window.location.reload(true);
                    });
                }, 5000);                
            }
        });
    });
}

function openStrategyHistoryModal(strategy_id, strategy_name, active=false) {
    /*
    clearGlobalModal();
    $("#global-modal-title").text("End Active Strategy")
    $('#global-modal-body').html(`
                <table class="table m-1">
                    <thead>
                        <tr>
                            <th scope="col">Trade No.</th>
                            <th scope="col">Trade Timestamp</th>
                            <th scope="col">Trade Summary</th>
                        </tr>
                    </thead>
                    <tbody id="trades-tbody">
                    </tbody>
                </table>`);
    $("#global-modal-footer").html(`
        <button type="button" class="btn btn-secondary" data-dismiss="modal" onClick="clearInterval(tradeHistoryInterval)">Dismiss</button>
    `);
    
    $("#global-modal").modal();   */
    $("#stocks-pane").slideUp('slow');
    $("#strategy-detail-pane").html(`
                <button type="button" class="close" aria-label="Close" onClick='$("#strategy-detail-pane").slideUp();$("#stocks-pane").slideDown();clearInterval(tradeHistoryInterval)'>
                  <span aria-hidden="true">&times;</span>
                </button>
                <h2 id="strategy-detail-header">${strategy_name} History</h2>
                <canvas id="strategy-trade-history-graph" width="400" height="400"></canvas>

                <table class="table m-1">
                    <thead>
                        <tr>
                            <th scope="col">Trade No.</th>
                            <th scope="col">Trade Timestamp</th>
                            <th scope="col">Trade Summary</th>
                        </tr>
                    </thead>
                    <tbody id="trades-tbody">
                    </tbody>
                </table>`).slideDown('slow');
    
    
    loadTradeHistory(strategy_id)
    
    if(active) {
        tradeHistoryInterval = setInterval( function() {loadTradeHistory(strategy_id)}, 5000);
    }
}

function loadTradeHistory(strategy_id) {
    $("#trades-tbody").html("");
    $.get(`/api/strategies/${strategy_id}/trades`, function(trades) {
        if(trades.length == 0) {
            $("#trades-tbody").html("<tr><td colspan='3'>This strategy has no trades to display.</td></tr>")
            $("#strategy-trade-history-graph").hide();
        } else {
            $.get(`/api/strategies/${strategy_id}/trade_evals`, function(tradeVals) {
                let tradeIndices = Array.apply(null, {length: tradeVals.length}).map(Number.call, Number)
                var ctx = document.getElementById("strategy-trade-history-graph").getContext('2d');
                var myChart = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: tradeIndices,
                        datasets: [{
                            label: 'Time',
                            data: tradeVals,
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1
                        }]
                    }
                });
                
            });
            
            
            
            
            
            
            
            
            
            
            
            
            $("#strategy-trade-history-graph").show();
            $.each(trades, function(index, trade) { 
                $("#trades-tbody").prepend(`
                    <tr>
                        <td scope="row">${index + 1}</td>
                        <td>${trade.timeTraded}</td>
                        <td>${trade.buying ? 'Buying' : 'Selling'} ${trade.numShares} shares at ${trade.tradePrice.toFixed(2)} for a grand total of \$${(trade.numShares * trade.tradePrice).toFixed(2)}</td>
                </tr>`);
            });            
        }        
    });
    
}

function openCreateNewStrategyModal() {

    $.get("templates/strategy-form.mustache", function(template) {
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
                    match: {enabled: true },
                    onSelectItemEvent: function() { 
                        newStrategySelectStock($("#strategy-stock-input").getSelectedItemData().Symbol, $("#strategy-stock-input").getSelectedItemData().Name)
                    },
                    onKeyEnterEvent: function() {
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
        $("#strategy-stock-input").easyAutocomplete(options_fullname).css("min-width","300px");
        $(".easy-autocomplete-container").css("min-width","300px");
        $("#strategy-quantity-input").on('input', function() {
            let stock_base_price = $("#new-investment-value").data("stock-base-price")
            if(stock_base_price)
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
    refreshStockPrice = setInterval(function() {
        $.get(`/api/stockprices/${ticker}/latest`, function(price) {
            $("#new-investment-value").data("stock-base-price", parseFloat(price));
            $("#new-investment-value").val($("#strategy-quantity-input").val() * parseFloat(price).toFixed(2));
        });
        
    }, 3000);
}



function openEditStrategyModal(strategy_id) {
    $.get("templates/strategy-edit-form.mustache", function(template) {
        Mustache.parse(template);   // optional, speeds up future uses
        clearGlobalModal();
        $("#global-modal-title").text("Edit Active Strategy")
        $.get("/api/strategies/" + strategy_id, function(strategy) {
            var strategy_algo_dict = {"TMA": "Two-Moving Averages", "PB": "Price Breakout", "BB": "Bollinger Bands"}           
            
            var rendered = Mustache.render(template, {
                strategyName: strategy.strategyName, strategyType: strategy_algo_dict[strategy.algo],
                startingPosition: strategy.startingPosition, strategyStock: strategy.stock.ticker,
                stockQuantity: strategy.numShares, lossExitThreshold: strategy.exitThresholdHigh, 
                gainExitThreshold: strategy.exitThresholdLow});
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
    let updated_strategy = {"strategyName": strategy_name, "exitThresholdHigh": gain_threshold_exit, "exitThresholdLow": loss_threshold_exit};
    $.ajax({        
        headers: { 
            'Accept': 'application/json',
            'Content-Type': 'application/json' 
        },
        url: `/api/strategies/${strategy_id}`,
        method: "PUT",
        data: JSON.stringify(updated_strategy), 
        success: function() {
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
        success: function() {
            window.location.reload(true);
        }
    });
}


function createStrategyWarning(label, message) {
    $("#new-strategy-warning-label").text(label);
    $("#new-strategy-warning-message").text(message);
    $("#new-strategy-warnings").show();
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
    
    if(!strategy_name) {
        createStrategyWarning("Missing Name!", "Please enter a name to identify your strategy!")
        return
    } else if(!strategy_share_quantity) {
        createStrategyWarning("Missing Quantity!", "Please enter a quantity of stock you'd like to begin your investment with!")
        return
    } else if(strategy_share_quantity < 1) {
        createStrategyWarning("Invalid Share Quantity!", "Please enter a quantity of stock you'd like to begin your investment with!")
        return
    }
    
    // TODO: Resolve Stock Name TBD
    // TODO: Fixed threshold rounding issue
    // TODO: Fix initiation price
    let new_stock = {"ticker": $("#strategy-stock-input").data("Symbol"), "stockName": $("#strategy-stock-input").val()};
    let new_strategy = {"strategyName": strategy_name, "algo": strategy_algo, "buying": ($("#strategy-starting-position-select").val() == "Buying"),
                        "stock": new_stock, "startTime": strategy_start, "initiationPrice": $("#new-investment-value").data("stock-base-price"), 
                        "numShares": strategy_share_quantity, "exitThresholdHigh": gain_threshold_exit, "exitThresholdLow": loss_threshold_exit, "active": true};
    $.ajax({        
        headers: { 
            'Accept': 'application/json',
            'Content-Type': 'application/json' 
        },
        url: "/api/strategies",
        method: "POST",
        data: JSON.stringify(new_strategy), 
        success: function() {
            window.location.reload(true);
        }
    });
}

// This function is meant to clear the global modal before a function uses it so that information from a previous usage of the modal doesn't leak into the newly displayed information.
function clearGlobalModal() {
    $("#global-modal-title").html("");
    $("#global-modal-body").html("");
    $("#global-modal-footer").html("");
}