export class ParserUtil {

    constructor() {}
    
    static separateName(fullName: string): string[] {
        let [firstName, ...lastNames] = fullName.split(" ")
        return [firstName, lastNames.join(" ")];
    }
    static capitalizeWord(word: string) : string {
        return word.charAt(0).toUpperCase() + word.slice(1);
    }
}