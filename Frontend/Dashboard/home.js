// const scrollContainer = document.getElementById("courses");

//     let scrollAmount = 0;
//     const scrollStep = 600;
//     const delay = 3000;

//     setInterval(() => {
//         scrollAmount += scrollStep;
//         if (scrollAmount >= scrollContainer.scrollWidth - scrollContainer.clientWidth) {
//             scrollAmount = 0;
//         }

//         scrollContainer.scrollTo({
//             left: scrollAmount,
//             behavior: "smooth"
//         });
//     }, delay);

const btn = document.getElementById("scroll");

  btn.addEventListener("click", function () {
    window.scrollTo({
      top: 0,
      behavior: "smooth"
    });
  });