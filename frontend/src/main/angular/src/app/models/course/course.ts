import { User } from "../user/user";

export interface Course {
  id: number;
  inactive?: boolean;
  authors?: string[];
  courseType?: string;
  name: string;
  slug: string;
  mentors: User[];
  svg: string;
  description?: string;
  additionalMaterialsExist: boolean;
}
