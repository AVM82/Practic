import { ReferenceTitle } from "./referenceTitle";

export interface AdditionalMaterials {
    id: number;
    number: number;
    name: string;
    refs: ReferenceTitle[];
    checked: boolean;
}