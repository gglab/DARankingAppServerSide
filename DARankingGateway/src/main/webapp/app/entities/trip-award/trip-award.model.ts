import { BaseEntity } from './../../shared';

export class TripAward implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public points?: number,
    ) {
    }
}
