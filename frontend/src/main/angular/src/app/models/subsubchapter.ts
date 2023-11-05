import {ReferenceTitle} from "./referenceTitle";

export interface SubSubChapter {
  id: number;
  number: number;
  name: string;
  refs?: ReferenceTitle[];
}
