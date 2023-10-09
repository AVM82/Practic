export class CourseProp {
    name!: string;
    slug!: string;
    route!: string;

    constructor(
        name: string,
        slug: string,
        route: string
    ) {
        this.name = name;
        this.slug = slug;
        this.route = route;
    }
  }
  