var selectedArticleId = null;
var articles = []; // Tableau pour stocker localement la liste des articles

document.addEventListener("DOMContentLoaded", function () {
    loadArticles();
    clearDetails();
});

function loadArticles() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/api", true);

    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
            try {
                articles = JSON.parse(xhr.responseText);
                var tableHtml = "<tr><th>ID</th><th>Intitulé</th><th>Prix</th><th>Stock</th></tr>";

                for (var i = 0; i < articles.length; i++) {
                    tableHtml += "<tr onclick='showDetails(" + articles[i].id + ")'>";
                    tableHtml += "<td>" + articles[i].id + "</td>";
                    tableHtml += "<td>" + articles[i].intitule + "</td>";
                    tableHtml += "<td>" + articles[i].prix + "</td>";
                    tableHtml += "<td>" + articles[i].stock + "</td>";
                    tableHtml += "</tr>";
                }

                document.getElementById("stockTable").innerHTML = tableHtml;
            } catch (error) {
                console.error("Erreur lors de l'analyse de la réponse JSON", error);
            }
        } else {
            console.error("Erreur lors de la récupération des articles");
        }
    };

    xhr.onerror = function () {
        console.error("Erreur lors de la récupération des articles", xhr.statusText);
    };

    xhr.send();
}

function showDetails(articleId) {
    selectedArticleId = articleId;

    // Recherche des détails de l'article dans le tableau local
    var articleDetails = articles.find(function (article) {
        return article.id === articleId;
    });

    if (articleDetails) {
        displayArticleDetails(articleDetails);
    } else {
        console.error("Détails de l'article non trouvés localement.");
    }
}

function displayArticleDetails(articleDetails) {
    document.getElementById("selectedItem").innerHTML =
        "<h3>Article sélectionné</h3>" +
        "<p>ID: " + articleDetails.id + "</p>" +
        "<p>Intitulé: " + articleDetails.intitule + "</p>" +
        "<p>Prix: " + articleDetails.prix + "</p>" +
        "<p>Stock: " + articleDetails.stock + "</p>" +
        "<img src='images/" + articleDetails.image + "' alt='Image de l'article'>";

    document.getElementById("priceInput").value = articleDetails.prix;
    document.getElementById("stockInput").value = articleDetails.stock;
}

function updateStock() {
    if (selectedArticleId === null) {
        alert("Veuillez sélectionner un article avant de mettre à jour le stock.");
        return;
    }

    var prix = document.getElementById("priceInput").value;
    var stock = document.getElementById("stockInput").value;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/api", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                alert("Mise à jour réussie!");
                // Mettez à jour la table ou effectuez d'autres actions nécessaires
                loadArticles();
                clearDetails();
            } else {
                console.error("Erreur lors de la mise à jour du stock");
                alert("Échec de la mise à jour.");
            }
        }
    };

    xhr.onerror = function () {
        console.error("Erreur lors de la mise à jour du stock");
        alert("Échec de la mise à jour.");
    };

    var data = "idArticle=" + selectedArticleId + "&prix=" + prix + "&stock=" + stock;
    xhr.send(data);
}


function clearDetails() {
    document.getElementById("selectedItem").innerHTML = "";
    document.getElementById("priceInput").value = "";
    document.getElementById("stockInput").value = "";
    selectedArticleId = null;
}
