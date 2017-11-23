function showAnonymousSearchResearchTab() {
    $("#anonymousResearchItemPanel").html('')
}

function showAnonymousSearchExpertTab() {
    $("#anonymousExpertPanel").html('')
}

function viewAnonymousResearchItem(uuid) {
    $.get("/research/" + uuid,
        function(data) {
            var text = '<table style="width:100%">'
            text = text.concat('<tr><td>Title</td><td>' + data.title + '</td></tr>')
            text = text.concat('<tr><td>Description</td><td>' + data.description + '</td></tr>')
            text = text.concat('<tr><td>Key words</td><td>' + data.keywords + '</td></tr>')
            text = text.concat('<tr><td>Price</th><td>' + data.price + '</td></tr>')
            text = text.concat('<tr><td>MD5</td><td>' + data.checksum + '</td></tr>')
            text = text.concat('<tr><td>Expert</td><td>' + data.expert.name + '</td></tr>')
            text = text.concat('</table>')
            $("#anonymousResearchItemPanel").html(text)
        }
    )
}

function showAnonymousResearchItems(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Title</th><th>Expert</th><th>Price</th>' +
    '<th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].title + '</td>' +
        '<td>' + data[val].expert.name + '</td>' +
        '<td>' + data[val].price + '</td>' +
        '<td>' + '<a href="#" onclick="viewAnonymousResearchItem(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">View</a>' + '</td>' +
        '</tr>'
        )
    })
    items.concat('</tbody></table>')
    $("#anonymousResearchItemsPanel").html(items)
    showUserBalance()
}

function viewAnonymousExpert(address) {
    $.get("/expert/" + address,
        function(data) {
            var text = '<table style="width:100%">'
            text = text.concat('<tr><td><b>Name</b></td><td>' + data.name + '</td></tr>')
            text = text.concat('<tr><td><b>Email</b></td><td>' + data.email + '</td></tr>')
            text = text.concat('<tr><td><b>Key words</b></td><td>' + data.keywords + '</td></tr>')
            text = text.concat('<tr><td><b>Description</b></td><td>' + data.description + '</td></tr>')
            text = text.concat('<tr><td></td><td>Researches</td></tr>')
            text = text.concat('</table>')
            $("#anonymousExpertPanel").html(text)
        }
    )
}

function showAnonymousExperts(data) {
    var items = '<table style="width:100%">' +
    '<thead><tr>' +
    '<th>Name</th><th>Email</th><th>Description</th>' +
    '<th></th>\n' +
    '</tr></thead>' +
    '<tbody>'
    $.each(data, function(val) {
        items = items.concat(
        '<tr>' +
        '<td>' + data[val].name + '</td>' +
        '<td>' + data[val].emailAddress + '</td>' +
        '<td>' + data[val].description + '</td>' +
        '<td>' + '<a href="#" onclick="viewAnonymousExpert(&#39;' + data[val].address + '&#39;)" class="btn btn-primary">View</a>' + '</td>'
        )
    })
    items.concat('</tbody></table>')
    $("#anonymousExpertsPanel").html(items)
    showUserBalance()
}

function anonymousSearchResearchItems() {
    address = getAddress()
    keywords = $("#anonymousResearchKeywords").val()

    $.get("/research/keywords/" + keywords,
        function(data) {
            showAnonymousResearchItems(data)
            $("#anonymousResearchKeywords").val('')
        }
    )
}

function anonymousSearchExperts() {
    address = getAddress()
    keywords = $("#anonymousExpertKeywords").val()

    $.get("/expert/keywords/" + keywords,
        function(data) {
            showAnonymousExperts(data)
            $("#anonymousExpertKeywords").val('')
        }
    )
}
