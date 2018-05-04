/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { DriverDialogComponent } from '../../../../../../main/webapp/app/entities/driver/driver-dialog.component';
import { DriverService } from '../../../../../../main/webapp/app/entities/driver/driver.service';
import { Driver } from '../../../../../../main/webapp/app/entities/driver/driver.model';
import { UserService } from '../../../../../../main/webapp/app/shared';
import { DriverAwardService } from '../../../../../../main/webapp/app/entities/driver-award';

describe('Component Tests', () => {

    describe('Driver Management Dialog Component', () => {
        let comp: DriverDialogComponent;
        let fixture: ComponentFixture<DriverDialogComponent>;
        let service: DriverService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [DriverDialogComponent],
                providers: [
                    UserService,
                    DriverAwardService,
                    DriverService
                ]
            })
            .overrideTemplate(DriverDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DriverDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DriverService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Driver(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.driver = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'driverListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Driver();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.driver = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'driverListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
