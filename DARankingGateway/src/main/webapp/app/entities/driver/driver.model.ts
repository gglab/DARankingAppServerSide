import { BaseEntity, User } from './../../shared';

export class Driver implements BaseEntity {
    constructor(
        public id?: number,
        public rank?: number,
        public name?: string,
        public user?: User,
        public awards?: BaseEntity[],
    ) {
    }
}
