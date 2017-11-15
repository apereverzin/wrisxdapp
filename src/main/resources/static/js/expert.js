function riskExpertEnquiriesClicked() {
    getExpertEnquiries()
}

function riskExpertRiskKnowledgeClicked() {
    getExpertRiskKnowledgeItems()
}

function riskExpertPurchasesClicked() {
    getExpertPurchases()
}

function riskExpertBidsClicked() {
    getExpertEnquiryBids()
}

function registerRiskExpert() {
    address = getAddress(defaultAddress);
    name = $("#riskExpertName").val();

    $.post("/riskExpert",
           {
               'name': name,
               'address': address,
               'emailAddress': '',
               'comment': ''
           }
    )

    contractInstance.registerRiskExpert(name, {from: address},
            function(error, result) {
                if(!error) {
                    //document.getElementById('result').value=result
                } else {
                    console.error(error);
                    //document.getElementById('result').value='Error'
                }
                document.getElementById('name').value=''
            }
    )
}

function depositRiskKnowledge() {
    address = getAddress(defaultAddress);

    var price = $("#riskExpertRiskKnowledgePrice").val();
    var title = $("#riskExpertRiskKnowledgeTitle").val();
    var description = $("#riskExpertRiskKnowledgeDescription").val();
    var keywords = $("#riskExpertRiskKnowledgeKeywords").val();

    address = getAddress(defaultAddress);

    var riskKnowledgeFile = uploadRiskKnowledgeFile();

    $.post("/riskKnowledge",
           {
               'address': address,
               'uuid': riskKnowledgeFile.uuid,
               'price': price,
               'title': title,
               'description': description,
               'keywords': keywords,
               'checksum': riskKnowledgeFile.zipFileChecksumMD5,
               'password': riskKnowledgeFile.password,

               'clientAddress': clientAddress,
               'enquiryId': 0,
               'bidId': 0,
           },
           function(data) {
               contractInstance.depositRiskKnowledge(price,
                                                     riskKnowledgeFile.uuid,
                                                     riskKnowledgeFile.password,
                                                     riskKnowledgeFile.zipFileChecksumMD5,

                                                     clientAddress,
                                                     0,
                                                     0,
                                                     {from: address},
                   function(error, result) {
                       if(!error) {
                           document.getElementById('result').value=result
                       } else {
                           console.error(error);
                           document.getElementById('result').value='Error. Have you registered as an expert?'
                       }
                       document.getElementById('riskExpertRiskKnowledgePrice').value=''
                       document.getElementById('riskExpertRiskKnowledgeTitle').value=''
                       document.getElementById('riskExpertRiskKnowledgeDescription').value=''
                       document.getElementById('riskExpertRiskKnowledgeKeywords').value=''
                       document.getElementById('riskKnowledge-upload-file-input').value=''
                   }
               );
           }
    )
}

function depositEnquiryBidRiskKnowledge(clientAddress, enquiryId, bidId) {
    address = getAddress(defaultAddress);

    var price = $("#riskExpertBidPrice").val();
    var title = $("#riskExpertBidTitle").val();
    var description = $("#riskExpertBidDescription").val();
    var keywords = $("#riskExpertBidKeywords").val();

    address = getAddress(defaultAddress);

    var riskKnowledgeFile = uploadBidFile();

    $.post("/riskKnowledge",
           {
               'address': address,
               'uuid': riskKnowledgeFile.uuid,
               'price': price,
               'title': title,
               'description': description,
               'keywords': keywords,
               'checksum': riskKnowledgeFile.zipFileChecksumMD5,
               'password': riskKnowledgeFile.password,

               'clientAddress': clientAddress,
               'enquiryId': enquiryId,
               'bidId': bidId,
           },
           function(data) {
               contractInstance.depositRiskKnowledge(price,
                                                     riskKnowledgeFile.uuid,
                                                     riskKnowledgeFile.password,
                                                     riskKnowledgeFile.zipFileChecksumMD5,

                                                     clientAddress,
                                                     enquiryId,
                                                     bidId,
                                                     {from: address},
                   function(error, result) {
                       if(!error) {
                           document.getElementById('result').value=result
                       } else {
                           console.error(error);
                           document.getElementById('result').value='Error. Have you registered as an expert?'
                       }
                       document.getElementById('riskExpertBidPrice').value=''
                       document.getElementById('riskExpertBidTitle').value=''
                       document.getElementById('riskExpertBidDescription').value=''
                       document.getElementById('riskExpertBidKeywords').value=''
                       document.getElementById('bid-upload-file-input').value=''
                   }
               );
           }
    )
}

function uploadBidFile() {
    var res;
    var formData = new FormData($("#bid-upload-file-form")[0]);
    $.ajax({
        url: "/uploadFile",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        async: false,
        success: function (data) {
            $("#bid-upload-file-message").text("File succesfully uploaded");
            res = data;
        },
        error: function () {
            $("#bid-upload-file-message").text("File not uploaded (perhaps it's too much big)");
        }
    });
    return res;
}

function uploadRiskKnowledgeFile() {
    var res;
    var formData = new FormData($("#riskKnowledge-upload-file-form")[0]);
    $.ajax({
        url: "/uploadFile",
        type: "POST",
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        async: false,
        success: function (data) {
            $("#riskKnowledge-upload-file-message").text("File succesfully uploaded");
            res = data;
        },
        error: function () {
            $("#riskKnowledge-upload-file-message").text("File not uploaded (perhaps it's too much big)");
        }
    });
    return res;
}

function showExpertRiskKnowledgeItems(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Title</th><th>Price</th>' +
    '<th></th><th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].title + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + '<a href="#" onclick="showExpertRiskKnowledge(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">View</a>' + '</td>' +
        '<td>' + '<a href="#" onclick="getRiskKnowledgePurchases(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">Purchases</a>' + '</td>' +
        '</tr>\n'
        );
    });
    items.concat('</tbody></table>')
    document.getElementById('expertRiskKnowledgeItems').innerHTML=items
    showUserBalance()
}

function showExpertEnquiryBids(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Key words</th><th>Comment</th><th>Price</th><th>Expert comment</th><th>Selected</th>' +
    '<th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].riskKnowledgeEnquiry.keywords + '</td>' +
        '<td>' + data[val].riskKnowledgeEnquiry.description + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + data[val].comment + '</td>' +
        '<td>' + data[val].selected + '</td>')
        if (data[val].selected && data[val].riskKnowledge == null) {
            items = items.concat('<td>' + '<a href="#" onclick="depositRiskKnowledge(&#39;' + data[val].riskKnowledgeEnquiry.client.address + '&#39;,&#39;' + data[val].riskKnowledgeEnquiry.id + '&#39;,&#39;' + data[val].id + '&#39;)" class="btn btn-primary">Deposit risk knowledge</a>' + '</td>')
        } else if (data[val].riskKnowledge != null) {
            items = items.concat('<td>Deposited</td>')
        } else {
            items = items.concat('<td></td>')
        }
        items = items.concat('</tr>\n')
    });
    items.concat('</tbody></table>')
    document.getElementById('expertEnquiryBids').innerHTML=items
    showUserBalance()
}

function showExpertPurchases(data, elementId) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Title</th><th>Key words</th><th>Price</th><th>Client</th><th>Date</th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].riskKnowledge.title + '</td>' +
        '<td>' + data[val].riskKnowledge.keywords + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + data[val].client.name + '</td>' +
        '<td>' + data[val].timestamp + '</td>')
        items = items.concat('</tr>\n')
    });
    items.concat('</tbody></table>')
    document.getElementById(elementId).innerHTML=items
    showUserBalance()
}

function showExpertRiskKnowledge(uuid) {
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
    );
}

function showExpertEnquiries(data) {
    var items = 'Bid: <input type="text" id="enquiryBid"/>&nbsp;' +
    'Comment:<input type="text" id="enquiryBidComment"/>' +
    '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Key words</th><th>Description</th><th>Bid</th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].keywords + '</td>' +
        '<td>' + data[val].description + '</td>');
        if (data[val].enquiryBid == null) {
        items = items.concat(
        '<td>' + '<a href="#" onclick="placeBid(&#39;' + data[val].id + '&#39;)" class="btn btn-primary">Place bid</a>' + '</td>'
        )
        } else {
        items = items.concat(
        '<td>' + data[val].enquiryBid.price + '&nbsp;' + data[val].enquiryBid.comment + '</td>'
        )
        }
        items = items.concat(
        '</tr>\n'
        );
    });
    items.concat('</tbody></table>')
    document.getElementById('expertEnquiries').innerHTML=items
    showUserBalance()
}

function placeBid(enquiryId) {
    address = getAddress(defaultAddress);

    var enquiryBid = $("#enquiryBid").val();
    var comment = $("#enquiryBidComment").val();
    $.post("/enquiry/" + enquiryId + "/bid",
            {
                'address': address,
                'bid': enquiryBid,
                'comment': comment
            }
    );
    document.getElementById('enquiryBid').value=''
    document.getElementById('enquiryBidComment').value=''
}

function getExpertRiskKnowledgeItems() {
    address = getAddress(defaultAddress)
    $.get("/riskKnowledge/expert/" + address,
        function(data) {
            showExpertRiskKnowledgeItems(data)
        }
    );
}

function getExpertEnquiryBids() {
    address = getAddress(defaultAddress)
    $.get("/enquiry/bid/expert/" + address,
        function(data) {
            showExpertEnquiryBids(data)
        }
    );
}

function getExpertPurchases() {
    address = getAddress(defaultAddress)
    $.get("/purchase/expert/" + address,
        function(data) {
            showExpertPurchases(data, "expertPurchases")
        }
    );
}

function getRiskKnowledgePurchases(uuid) {
    address = getAddress(defaultAddress)
    $.get("/purchase/riskKnowledge/" + uuid,
        function(data) {
            showExpertPurchases(data, "expertPurchases")
        }
    );
}

function getExpertEnquiries() {
    address = getAddress(defaultAddress)

    $.get("/enquiry/expert/" + address + "/keywords",
        function(data) {
            showExpertEnquiries(data)
        }
    )
}

function searchEnquiries() {
    address = getAddress(defaultAddress)
    keywords = $("#enquiryKeywords").val()

    $.get("/enquiry/expert/" + address + "/keywords/" + keywords,
        function(data) {
            showExpertEnquiries(data)
        }
    )
}
