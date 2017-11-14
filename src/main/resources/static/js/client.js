var enquiryBids
var globalEnquiryId

function clientRiskKnowledgeClicked() {
    getClientRiskKnowledgeItems()
}

function clientPurchasesClicked() {
    getClientPurchases()
}

function clientEnquiriesClicked() {
    getClientEnquiries()
}

function registerClient() {
    address = getAddress(clientAddress);
    name = $("#name").val();

    $.post("/client",
           {
               'name': name,
               'address': address,
               'emailAddress': '',
               'comment': ''
           }
    )

    contractInstance.registerClient(name, {from: address},
            function(error, result) {
                if(!error) {
                } else {
                    console.error(error);
                }
                document.getElementById('name').value=''
            }
    )
}

function viewClientRiskKnowledge(uuid) {
    $.get("/riskKnowledge/" + uuid,
        function(data) {
            var text = '<table style="width:100%">' +
            '<thead><tr>' +
            '<th>Title</th><th>Description</th><th>Key words</th><th>Price</th><th>Check sum</th>' +
            '</tr></thead><tbody>'
            text = text.concat('<tr>')
            text = text.concat('<td>' + data.title + '</td>')
            text = text.concat('<td>' + data.description + '</td>')
            text = text.concat('<td>' + data.keywords + '</td>')
            text = text.concat('<td>' + data.price + '</td>')
            text = text.concat('<td>' + data.checksum + '</td>')
            text = text.concat('</tr>')
            text = text.concat('</tbody></table>')
            document.getElementById('result').innerHTML = text
        }
    )
}

function buyTokens() {
    amount = $("#clientBuyTokensAmount").val() * tokenPrice;
    address = getAddress(defaultAddress);

    contractInstance.buyTokens({from: address, value: amount},
            function(error, result) {
                if(!error) {
                    //document.getElementById('result').innerHtml=result
                    showUserBalance()
                } else {
                    console.error(error);
                    //document.getElementById('result').innerHtml='Error. Have you got enough Ether?'
                }
                document.getElementById('amount').value='';
            }
    );
}

function payForRiskKnowledge(uuid) {
    address = getAddress(defaultAddress);

    $.post("/purchase",
        {
            "address": address,
            "uuid": uuid
        },
        function(data) {
            contractInstance.payForRiskKnowledge(uuid, {from: address},
                    function(error, result) {
                        if(!error) {
                            showUserBalance()
                        } else {
                            console.error(error);
                            document.getElementById('result').value='Error. Have you got enough tokens?'
                        }
                    }
            );
        }
    )
}

function getRiskKnowledge(fileName) {
    address = getAddress(clientAddress);

    contractInstance.getRiskKnowledge.call(fileName, {from: address},
            function(error, result) {
                if(!error) {
                    document.getElementById('result').value=result
                } else {
                    console.error(error);
                    document.getElementById('result').value='Error. Have you paid?'
                }
            }
    );
}

function showClientRiskKnowledgeItems(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Title</th><th>Expert</th><th>Price</th>' +
    '<th></th><th></th><th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].title + '</td>' +
        '<td>' + data[val].riskExpert.name + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + '<a href="#" onclick="viewClientRiskKnowledge(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">View</a>' + '</td>'
        )
        if (data[val].purchase == null) {
            items = items.concat(
            '<td>' + '<a href="#" onclick="payForRiskKnowledge(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">Pay</a>' + '</td>'
            )
        } else {
            items = items.concat(
            '<td>' + '<a href="#" onclick="getRiskKnowledge(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">Password</a>' + '</td>'
            )
        }
        items = items.concat(
        '<td>' + '<a href="/downloadFile/' + data[val].uuid + '" class="btn btn-primary">Download</a>' + '</td>' +
        '</tr>\n'
        )
    })
    items.concat('</tbody></table>')
    document.getElementById('clientRiskKnowledgeItems').innerHTML=items
    showUserBalance()
}

function showClientEnquiries(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Key words</th><th>Description</th>' +
    '<th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].keywords + '</td>' +
        '<td>' + data[val].description + '</td>' +
        '<td>' + '<a href="#" onclick="getEnquiryBids(' + data[val].id + ',&#39;' +
                    data[val].keywords + '&#39;,&#39;' + data[val].description +
                    '&#39;)" class="btn btn-primary">Bids</a>' + '</td>' +
        '</tr>\n'
        );
    });
    items = items.concat('</tbody></table>')
    items = items.concat('<p id="clientRiskKnowledgeEnquiryBids"/>')
    document.getElementById('clientRiskKnowledgeEnquiries').innerHTML=items
    showUserBalance()
}

function showClientPurchases(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Title</th><th>Key words</th><th>Price</th><th>Expert</th><th>Date</th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].riskKnowledge.title + '</td>' +
        '<td>' + data[val].riskKnowledge.keywords + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + data[val].riskExpert.name + '</td>' +
        '<td>' + data[val].timestamp + '</td>')
        items = items.concat('</tr>\n')
    });
    items.concat('</tbody></table>')
    document.getElementById('clientPurchases').innerHTML=items
    showUserBalance()
}

function showEnquiryBids(enquiryId, keywords, description, data) {
    var items = '<b>Enquiry</b>&nbsp;Keywords: ' + keywords +
    '&nbsp:Description: ' + description +
    '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Bid</th><th>Comment</th><th>Expert</th>' +
    '<th></th><th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    var submitted = false
    $.each(data, function(val) {
        if (data[val].selected) {
            submitted = true
        }
    })

    enquiryBids = new Array()
    $.each(data, function(val) {
        var enquiryBidCheckboxId = 'bid' + data[val].id
        if (enquiryBids.length > 0) {
            enquiryBids = enquiryBids.concat(',')
        }
        var enquiryBid = {
            enquiryBidCheckboxId: enquiryBidCheckboxId,
            bidId: data[val].id,
            expert: data[val].riskExpert.address,
            price: data[val].price
        };
        enquiryBids.push(enquiryBid)
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + data[val].comment + '</td>' +
        '<td>' + data[val].riskExpert.name + '</td>')
        if (!submitted) {
            items = items.concat('<td><input type="checkbox" id="' + enquiryBidCheckboxId + '"/></td>')
        } else if (data[val].riskKnowledge != null) {
            items = items.concat('<td>' +
            '<a href="#" onclick="getRiskKnowledge(&#39;' + data[val].riskKnowledge.uuid + '&#39;)" class="btn btn-primary">Password</a>' +
            '&nbsp;' +
            '<a href="/downloadFile/' + data[val].riskKnowledge.uuid + '" class="btn btn-primary">Download</a>' +
            '</td>')
        } else {
            items = items.concat('<td></td>')
        }
        items = items.concat(
        '<td></td>' +
        '</tr>\n'
        );
    });
    items = items.concat('</tbody></table>')
    if (!submitted) {
        items = items.concat('<a href="#" onclick="placeEnquiry()" class="btn btn-primary">Submit</a>')
    }
    document.getElementById('clientRiskKnowledgeEnquiryBids').innerHTML=items
    globalEnquiryId = enquiryId
    globalKeywords = keywords
    showUserBalance()
}

function postEnquiry() {
    var keywords = $("#enquiryKeywords").val();
    var description = $("#enquiryDescription").val();

    address = getAddress(clientAddress);

    $.post("/enquiry",
        {
            'address': address,
            'keywords': keywords,
            'description': description
        },
        function(data) {
            document.getElementById('enquiryKeywords').value = ''
            document.getElementById('enquiryDescription').value = ''
            getClientEnquiries()
        }
    )
}

function placeEnquiry() {
    var ind = 0
    var bidId0 = 0
    var expert0 = ''
    var price0 = 0
    var bidId1 = 0
    var expert1 = ''
    var price1 = 0
    var bidId2 = 0
    var expert2 = ''
    var price2 = 0

    for (i = 0; i < enquiryBids.length; i++) {
        enquiryBid = enquiryBids[i]
        var enquiryBidCheckbox = document.getElementById(enquiryBid.enquiryBidCheckboxId);
        if (enquiryBidCheckbox.value == "on") {
            if (ind == 0) {
                bidId0 = enquiryBid.bidId
                expert0 = enquiryBid.expert
                price0 = enquiryBid.price
            } else if (ind == 1) {
                bidId1 = enquiryBid.bidId
                expert1 = enquiryBid.expert
                price1 = enquiryBid.price
            } else if (ind == 2) {
                bidId2 = enquiryBid.bidId
                expert2 = enquiryBid.expert
                price2 = enquiryBid.price
            }
            $.post("/enquiry/bid/" + enquiryBid.bidId,
                function(data) {
                    console.log('enquiry bid id ' + enquiryBid.bidId)
                }
            )
        }
        if (ind == 2) {
            break
        }
        ind++
    }

    if (ind > 0) {
        $.get("/enquiry/" + globalEnquiryId,
            function(data) {
                contractInstance.placeEnquiry(globalEnquiryId,
                                              data.keywords,
                                              bidId0,
                                              expert0,
                                              price0,
                                              bidId1,
                                              expert1,
                                              price1,
                                              bidId2,
                                              expert2,
                                              price2,
                                              {from: address},
                    function(error, result) {
                        if(!error) {
                            getClientEnquiries()
                        } else {
                            console.error(error);
                        }
                        document.getElementById('enquiryDescription').value=''
                    }
                )
            }
        )
    }
}

function getEnquiryBids(enquiryId, keywords, description) {
    $.get("/enquiry/" + enquiryId + "/bid",
        function(data) {
            showEnquiryBids(enquiryId, keywords, description, data)
        }
    );
}

function searchRiskKnowledgeItems() {
    address = getAddress(defaultAddress)
    keywords = $("#keywords").val()

    $.get("/riskKnowledge/client/" + address + "/keywords/" + keywords,
        function(data) {
            showClientRiskKnowledgeItems(data)
        }
    )
}

function getClientRiskKnowledgeItems() {
    address = getAddress(defaultAddress)

    $.get("/riskKnowledge/client/" + address + "/keywords",
        function(data) {
            showClientRiskKnowledgeItems(data)
        }
    )
}

function getClientEnquiries() {
    address = getAddress(clientAddress);

    $.get("/enquiry/client/" + address,
        function(data) {
            showClientEnquiries(data)
        }
    );
}

function getClientPurchases() {
    address = getAddress(clientAddress);

    $.get("/purchase/client/" + address,
        function(data) {
            showClientPurchases(data)
        }
    );
}
