import {Chapter} from "./chapter";

export interface Course {
  id: number;
  inactive?: boolean;
  authors?: string;
  courseType?: string;
  name: string;
  shortName: string;
  chapters: Chapter[];
}
