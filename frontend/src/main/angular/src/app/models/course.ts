import { Mentor } from "./mentor";

export interface Course {
  id: number;
  inactive?: boolean;
  authors?: string[];
  courseType?: string;
  name: string;
  slug: string;
  mentors: Mentor[];
  svg: string;
  description?: string;
  additionalMaterialsExist: boolean;
}
