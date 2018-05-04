import { BaseEntity } from './../../shared';

export class Trip implements BaseEntity {
    constructor(
        public id?: number,
        public start?: any,
        public duration?: string,
        public distance?: number,
        public speedingDistance?: number,
        public maxSpeedingVelocity?: number,
        public suddenBrakings?: number,
        public suddenAccelerations?: number,
        public points?: number,
        public driver?: BaseEntity,
        public awards?: BaseEntity[],
    ) {
    }
}
