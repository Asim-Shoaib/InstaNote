document.addEventListener("DOMContentLoaded", function() {
    document.querySelector(".bar button").addEventListener("click", function() {
        let newItem = document.createElement("div");
        newItem.classList.add("previous-note");
        newItem.innerText = "Random text";

        document.getElementById("note-container").appendChild(newItem);
    });
});

