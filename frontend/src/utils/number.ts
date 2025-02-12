export const getOrdinalNumber= (n: number): string =>{
    if (n <= 0) return n.toString(); 
  
    const suffixes = ['th', 'st', 'nd', 'rd'];
    const remainder100 = n % 100;
    const remainder10 = n % 10;

    if (remainder100 >= 11 && remainder100 <= 13) {
        return `${n}th`;
    }

    return `${n}${suffixes[remainder10] || 'th'}`;
}