import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Driver } from './driver.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Driver>;

@Injectable()
export class DriverService {

    private resourceUrl =  SERVER_API_URL + 'api/drivers';

    constructor(private http: HttpClient) { }

    create(driver: Driver): Observable<EntityResponseType> {
        const copy = this.convert(driver);
        return this.http.post<Driver>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(driver: Driver): Observable<EntityResponseType> {
        const copy = this.convert(driver);
        return this.http.put<Driver>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Driver>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Driver[]>> {
        const options = createRequestOption(req);
        return this.http.get<Driver[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Driver[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Driver = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Driver[]>): HttpResponse<Driver[]> {
        const jsonResponse: Driver[] = res.body;
        const body: Driver[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Driver.
     */
    private convertItemFromServer(driver: Driver): Driver {
        const copy: Driver = Object.assign({}, driver);
        return copy;
    }

    /**
     * Convert a Driver to a JSON which can be sent to the server.
     */
    private convert(driver: Driver): Driver {
        const copy: Driver = Object.assign({}, driver);
        return copy;
    }
}
