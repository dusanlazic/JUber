export class ParserUtil {

    constructor() {}
    
    static separateName(fullName: string): string[] {
        let [firstName, ...lastNames] = fullName.split(" ")
        return [firstName, lastNames.join(" ")];
    }
    static capitalizeWord(word: string) : string {
        return word.charAt(0).toUpperCase() + word.slice(1);
    }

    static getUrlParameter(paramName: string, url: string) : string {
        paramName = paramName.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + paramName + '=([^&#]*)');
        const [_, search] = url.split('?');
        var results = regex.exec("?".concat(search));
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
      };
}