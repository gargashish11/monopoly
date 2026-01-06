export async function fetch(url, method = 'GET') {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`HTTP status ${response.status}: ${response.statusText}`);
    }
    return await response.json();
}