$(document).ready(function() {
    loadStrategies();
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

// This is called once a user clicks the 'Create Strategy' button which appears as a send icon on the dashboard. This function creates a modal to confirm the details. A button in the footer of the modal which says 'Create Strategy' will call createStrategy() which will actually call the REST service to create the strategy.
function confirmCreateStrategy() {
    
    // TODO: Add input validation here.
    var strategy_long_names_dict = {
        "TMA": "Two-Moving Averages",
        "PB": "Price Breakout",
        "BB": "Bollinger Bands"
    };
    
    
    clearGlobalModal();
    let strategy_name = $("#strategy-name-input").val();
    let strategy_type = $("#strategy-type-select").find(":selected").text();
    let strategy_start_position = ("Buying" == $("#strategy-starting-position-select").find(":selected").text());
    let strategy_share_quantity = $("#strategy-quantity-input").val();
    
    // TODO: This will change from a select in the future.
    let strategy_share = $("#strategy-share-select").find(":selected").text();
    
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