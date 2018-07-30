$(document).ready(function() {
    loadStrategies();
    quantity_input_field = $("#strategy-quantity-input");
    quantity_input_field.change(function() {
        quantity = $("#strategy-quantity-input").val();
        stock = $("#strategy-share-select").text();
        let stock_price_regex = /.+\(\$(\d+\.\d+)\)/g
        stock_vals = stock_price_regex.exec(stock);
        console.log(quantity);
        console.log(stock);
        console.log(stock_vals);
        if(quantity && stock) {
            let stock_price = parseFloat(stock_vals[1]).toFixed(2);
            let investment_value = (stock_price * quantity).toFixed(2)
            $("#new-investment-value").val(investment_value);
        }
    });
});

function loadStrategies() {
    // TODO: Call the REST API once it's created.
}


function viewMoreStrategy(strategy_id) {
    // TODO: Create view more details modal.    
}

function editStrategy(strategy_id) {
    // TODO: Create an edit strategy modal.
    // TODO: Call the REST API once it's created.        
}

function endStrategy(strategy_id) {
    // TODO: Call the REST API once it's created.        
}


function createStrategyWarning(label, message) {
    $("#new-strategy-warning-label").text(label);
    $("#new-strategy-warning-message").text(message);
    $("#new-strategy-warnings").show();
}

// This is called once a user clicks the 'Create Strategy' button which appears as a send icon on the dashboard. This function creates a modal to confirm the details. A button in the footer of the modal which says 'Create Strategy' will call createStrategy() which will actually call the REST service to create the strategy.
function confirmCreateStrategy() {
    $("#new-strategy-warnings").hide();
    
    // TODO: Add input validation here.
    var strategy_long_names_dict = {
        "TMA": "Two-Moving Averages",
        "PB": "Price Breakout",
        "BB": "Bollinger Bands"
    };
    
    
    clearGlobalModal();
    let strategy_name = $("#strategy-name-input").val();
    let strategy_type = $("#strategy-type-select").find(":selected").text();
    let strategy_start_position = ("Buy" == $("#strategy-starting-position-select").find(":selected").text());
    let strategy_share_quantity = parseInt($("#strategy-quantity-input").val());
    
    // TODO: This will change from a select in the future.
    let strategy_share = $("#strategy-share-select").find(":selected").text();
    
    
    
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
    
    
    $("#global-modal-title").text("Confirm Strategy Details")
    $("#global-modal-body").html(`
    <div class="row">
        <div class="col-4">Strategy Name: </div>
        <div class="col-8">${strategy_name}</div>
    </div>
    <div class="row">
        <div class="col-4">Strategy Type: </div>
        <div class="col-8">${strategy_long_names_dict[strategy_type]}</div>
    </div>
    <div class="row">
        <div class="col-4">Starting Position: </div>
        <div class="col-8">${strategy_start_position ? 'Buying' : 'Selling'}</div>
    </div>
    <div class="row">
        <div class="col-4">Share: </div>
        <div class="col-8">${strategy_share}</div>
    </div>    
    <div class="row">
        <div class="col-4">Share Quantity: </div>
        <div class="col-8">${strategy_share_quantity}</div>
    </div>    
    `);
    $("#global-modal-footer").html(`
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-success" onClick="createStrategy(${strategy_name}, ${strategy_type}, ${strategy_start_position}, ${strategy_share}, ${strategy_share_quantity}">Create Strategy</button>
    `);
    $("#global-modal").modal();
}

// This function is called from the strategy confirmation modal (refer to confirmCreateStrategy()) and will call the REST API to create a strategy.
function createStrategy(strategy_name, strategy_type, strategy_start_position, strategy_share, strategy_share_quantity) {
    // TODO: Call the REST API once it's created.    
}

// This function is meant to clear the global modal before a function uses it so that information from a previous usage of the modal doesn't leak into the newly displayed information.
function clearGlobalModal() {
    $("#global-modal-title").html("");
    $("#global-modal-body").html("");
    $("#global-modal-footer").html("");
}