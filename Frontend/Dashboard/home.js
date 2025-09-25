const scrollContainer = document.getElementById("allcourses");

    let scrollAmount = 0;
    const scrollStep = 460;
    const delay = 3000;

    setInterval(() => {
        scrollAmount += scrollStep;
        if (scrollAmount >= scrollContainer.scrollWidth - scrollContainer.clientWidth) {
            scrollAmount = 0;
        }

        scrollContainer.scrollTo({
            left: scrollAmount,
            behavior: "smooth"
        });
    }, delay);