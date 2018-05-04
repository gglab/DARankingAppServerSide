/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { DriverAwardDialogComponent } from '../../../../../../main/webapp/app/entities/driver-award/driver-award-dialog.component';
import { DriverAwardService } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.service';
import { DriverAward } from '../../../../../../main/webapp/app/entities/driver-award/driver-award.model';
import { DriverService } from '../../../../../../main/webapp/app/entities/driver';

describe('Component Tests', () => {

    describe('DriverAward Management Dialog Component', () => {
        let comp: DriverAwardDialogComponent;
        let fixture: ComponentFixture<DriverAwardDialogComponent>;
        let service: DriverAwardService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [DriverAwardDialogComponent],
                providers: [
                    DriverService,
                    DriverAwardService
                ]
            })
            .overrideTemplate(DriverAwardDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DriverAwardDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DriverAwardService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new DriverAward(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.driverAward = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'driverAwardListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new DriverAward();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.driverAward = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'driverAwardListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
