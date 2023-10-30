export class CourseProp {
    name!: string;
    slug!: string;
    route!: string;
    color!:string;

    constructor(
        name: string,
        slug: string,
        route: string,
        color: string
    ) {
        this.name = name;
        this.slug = slug;
        this.route = route;
        this.color = color;
    }
  }
  