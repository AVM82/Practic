import { ReferenceTitle } from "./referenceTitle";

export class AdditionalMaterials {
    id?: number;
    number?: number;
    name?: string;
    refs?: ReferenceTitle[];
    checked?: boolean;

    constructor(
        _id: number,
        _number: number,
        _name: string,
        _refs: ReferenceTitle[],
        _checked: boolean
    ) {
        this.id = _id;
        this.number = _number;
        this.name = _name;
        this.refs = _refs;
        this.checked = _checked;
    }
    
}