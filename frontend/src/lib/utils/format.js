const formatDate = (dateString) => {
    let dateTime = new Date(dateString);
    const year = dateTime.getFullYear();
    const month = (dateTime.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based, so we add 1
    const day = dateTime.getDate().toString().padStart(2, '0');
    const hours = dateTime.getHours().toString();
    const minutes = dateTime.getMinutes().toString().padStart(2, '0');
    return `${day}-${month}-${year} ${hours}:${minutes}`;
}

export default formatDate