import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { DriverAward } from './driver-award.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<DriverAward>;

@Injectable()
export class DriverAwardService {

    private resourceUrl =  SERVER_API_URL + 'api/driver-awards';

    constructor(private http: HttpClient) { }

    create(driverAward: DriverAward): Observable<EntityResponseType> {
        const copy = this.convert(driverAward);
        return this.http.post<DriverAward>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(driverAward: DriverAward): Observable<EntityResponseType> {
        const copy = this.convert(driverAward);
        return this.http.put<DriverAward>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<DriverAward>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<DriverAward[]>> {
        const options = createRequestOption(req);
        return this.http.get<DriverAward[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DriverAward[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: DriverAward = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<DriverAward[]>): HttpResponse<DriverAward[]> {
        const jsonResponse: DriverAward[] = res.body;
        const body: DriverAward[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to DriverAward.
     */
    private convertItemFromServer(driverAward: DriverAward): DriverAward {
        const copy: DriverAward = Object.assign({}, driverAward);
        return copy;
    }

    /**
     * Convert a DriverAward to a JSON which can be sent to the server.
     */
    private convert(driverAward: DriverAward): DriverAward {
        const copy: DriverAward = Object.assign({}, driverAward);
        return copy;
    }
}
