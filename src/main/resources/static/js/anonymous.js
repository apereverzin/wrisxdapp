function showAnonymousSearchResearchTab() {

}

function showAnonymousSearchExpertTab() {

}

function viewAnonymousResearch(uuid) {
    $.get("/research/" + uuid,
        function(data) {
            var text = '<table style="width:100%">' +
            '<thead><tr>' +
            '<th>Title</th><th>Description</th><th>Key words</th><th>Price</th>' +
            '<th>Check sum</th><th></th>' +
            '</tr></thead><tbody>'
            text = text.concat('<tr>')
            text = text.concat('<td>' + data.title + '</td>')
            text = text.concat('<td>' + data.description + '</td>')
            text = text.concat('<td>' + data.keywords + '</td>')
            text = text.concat('<td>' + data.price + '</td>')
            text = text.concat('<td>' + data.checksum + '</td>')
            text = text.concat('<td>Expert</td>')
            text = text.concat('</tr>')
            text = text.concat('</tbody></table>')
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
        '<td>' + '<a href="#" onclick="viewAnonymousResearch(&#39;' + data[val].uuid + '&#39;)" class="btn btn-primary">View</a>' + '</td>'
        )
    })
    items.concat('</tbody></table>')
    $("#anonymousResearchItems").html(items)
    showUserBalance()
}

function viewAnonymousExpert(id) {
    $.get("/expert/" + id,
        function(data) {
            var text = '<table style="width:100%">' +
            '<thead><tr>' +
            '<th>Name</th><th>Emailn</th><th>Description</th><th>Key words</th>' +
            '<th></th>' +
            '</tr></thead><tbody>'
            text = text.concat('<tr>')
            text = text.concat('<td>' + data.name + '</td>')
            text = text.concat('<td>' + data.email + '</td>')
            text = text.concat('<td>' + data.description + '</td>')
            text = text.concat('<td>' + data.keywords + '</td>')
            text = text.concat('<td>Researches</td>')
            text = text.concat('</tr>')
            text = text.concat('</tbody></table>')
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
        '<td>' + '<a href="#" onclick="viewAnonymousExpert(&#39;' + data[val].id + '&#39;)" class="btn btn-primary">View</a>' + '</td>'
        )
    })
    items.concat('</tbody></table>')
    $("#anonymousExperts").html(items)
    showUserBalance()
}

function anonymousSearchResearchItems() {
    address = getAddress(defaultAddress)
    keywords = $("#anonymousResearchKeywords").val()

    $.get("/research/keywords/" + keywords,
        function(data) {
            showAnonymousResearchItems(data)
            $("#anonymousResearchKeywords").val('')
        }
    )
}

function anonymousSearchExperts() {
    address = getAddress(defaultAddress)
    keywords = $("#anonymousExpertKeywords").val()

    $.get("/expert/keywords/" + keywords,
        function(data) {
            showAnonymousExperts(data)
            $("#anonymousExpertKeywords").val('')
        }
    )
}
