import { BaseEntity } from './../../shared';

export class DriverAward implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public points?: number,
        public drivers?: BaseEntity[],
    ) {
    }
}
